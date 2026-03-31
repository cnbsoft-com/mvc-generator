package com.cnbsoft.generator.task;

import com.cnbsoft.generator.engine.ColumnInspector;
import com.cnbsoft.generator.engine.GeneratorConfig;
import com.cnbsoft.generator.engine.TemplateEngine;
import com.cnbsoft.generator.engine.generators.*;
import com.cnbsoft.plugin.generator.interactive.GenerationType;
import com.cnbsoft.plugin.generator.interactive.TableSelector;
import com.cnbsoft.generator.plugin.MvcGeneratorBaseTask;
import com.cnbsoft.generator.plugin.MvcGeneratorExtension;
import org.gradle.api.DefaultTask;
import org.gradle.api.GradleException;
import org.gradle.api.file.ConfigurableFileCollection;
import org.gradle.api.provider.Property;
import org.gradle.api.tasks.InputFiles;
import org.gradle.api.tasks.Internal;
import org.gradle.api.tasks.TaskAction;

import java.io.File;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.*;

/**
 * 대화형으로 테이블을 검색·확인하고 생성 종류를 선택해 MVC 코드를 생성하는 태스크.
 *
 * 실행 방법:
 *   ./gradlew generateMvcInteractive --no-daemon --console=plain
 */
public abstract class GenerateMvcInteractiveTask extends DefaultTask {

    @Internal
    public abstract Property<MvcGeneratorExtension> getGeneratorExtension();

    @InputFiles
    public abstract ConfigurableFileCollection getJdbcClasspath();

    @TaskAction
    public void execute() {
        MvcGeneratorExtension ext = getGeneratorExtension().get();
        ClassLoader jdbcLoader    = buildJdbcClassLoader();
        TableSelector selector    = new TableSelector(System.in, System.out);

        // DB 연결용 probe config (tableNames 검증 생략)
        GeneratorConfig probeConfig = buildProbeConfig(ext);

        try (ColumnInspector inspector = new ColumnInspector(probeConfig, jdbcLoader)) {

            boolean continueSession = true;
            while (continueSession) {

                List<String> confirmedTables;

                // 1. 패턴 입력 → 테이블 검색 → 확인 루프
                while (true) {
                    List<String> patterns = selector.promptForPatterns();
                    if (patterns.isEmpty()) {
                        System.out.println("Please enter at least one pattern.");
                        continue;
                    }

                    List<String> candidates = new ArrayList<>();
                    for (String pattern : patterns) {
                        List<String> found = inspector.listTablesLike(pattern);
                        for (String t : found) {
                            if (!candidates.contains(t)) candidates.add(t);
                        }
                    }

                    if (candidates.isEmpty()) {
                        System.out.println("No tables matched. Try again.");
                        continue;
                    }

                    confirmedTables = selector.confirmTables(candidates);
                    if (!confirmedTables.isEmpty()) break;
                }

                System.out.println();
                System.out.println("Confirmed: " + String.join(", ", confirmedTables));

                // 2. 생성 종류 선택
                Set<GenerationType> types = selector.selectGenerationTypes();

                // 3. 최종 config (confirmedTables 포함)
                GeneratorConfig config = buildFinalConfig(ext, confirmedTables);
                TemplateEngine engine  = new TemplateEngine(config);

                // 4. 생성 실행
                System.out.println();
                for (String tableName : confirmedTables) {
                    System.out.println("Generating for " + tableName + "...");
                    runGenerators(tableName, config, inspector, engine, types);
                }

                // 5. 계속 여부 확인
                continueSession = selector.promptContinue();
            }

        } catch (GradleException e) {
            throw e;
        } catch (Exception e) {
            throw new GradleException("Interactive MVC generation failed: " + e.getMessage(), e);
        }
    }

    private void runGenerators(String tableName, GeneratorConfig config,
                                ColumnInspector inspector, TemplateEngine engine,
                                Set<GenerationType> types) throws Exception {
        boolean all = types.contains(GenerationType.ALL);

        if (all || types.contains(GenerationType.MODEL))
            new ModelCodeGenerator(config, inspector, engine).generate(tableName);
        if (all || types.contains(GenerationType.CONTROLLER))
            new ControllerCodeGenerator(config, inspector, engine).generate(tableName);
        if (all || types.contains(GenerationType.SERVICE))
            new ServiceCodeGenerator(config, inspector, engine).generate(tableName);
        if (all || types.contains(GenerationType.PERSISTENCE)) {
            if ("annotation".equalsIgnoreCase(config.mapperType))
                new PersistenceAnnoCodeGenerator(config, inspector, engine).generate(tableName);
            else
                new PersistenceCodeGenerator(config, inspector, engine).generate(tableName);
        }
        // if (all || types.contains(GenerationType.QUERY))
            // new QueryCodeGenerator(config, inspector, engine).generate(tableName);
        if (all || types.contains(GenerationType.FORM_VIEW))
            new ViewCodeGenerator(config, inspector, engine).generate(tableName, ViewCodeGenerator.ViewType.FORM);
        if (all || types.contains(GenerationType.LIST_VIEW))
            new ViewCodeGenerator(config, inspector, engine).generate(tableName, ViewCodeGenerator.ViewType.LIST);
    }

    /** tableNames 검증을 건너뛴 DB 연결 전용 config */
    private GeneratorConfig buildProbeConfig(MvcGeneratorExtension ext) {
        return buildConfigBuilder(ext)
                .buildSkipTableCheck();
    }

    /** 확정된 테이블 목록을 포함한 최종 config */
    private GeneratorConfig buildFinalConfig(MvcGeneratorExtension ext, List<String> tableNames) {
        return buildConfigBuilder(ext)
                .tableNames(tableNames)
                .build();
    }

    private GeneratorConfig.Builder buildConfigBuilder(MvcGeneratorExtension ext) {
        File customTplDir = ext.getCustomTemplateDir().isPresent()
                ? ext.getCustomTemplateDir().get().getAsFile() : null;
        File outputDir  = ext.getOutputDir().get().getAsFile();
        File resourceDir = ext.getResourceOutputDir().isPresent()
                ? ext.getResourceOutputDir().get().getAsFile() : outputDir;
        File viewDir    = ext.getViewOutputDir().isPresent()
                ? ext.getViewOutputDir().get().getAsFile() : outputDir;

        return GeneratorConfig.builder()
                .dbDriver(ext.getDbDriver().get())
                .dbUrl(ext.getDbUrl().get())
                .dbUsername(ext.getDbUsername().get())
                .dbPassword(ext.getDbPassword().getOrElse(""))
                .outputDir(outputDir)
                .resourceOutputDir(resourceDir)
                .viewOutputDir(viewDir)
                .basePackage(ext.getBasePackage().get())
                .modelPath(ext.getModelSubPackage().get())
                .controllerPath(ext.getControllerSubPackage().get())
                .servicePath(ext.getServiceSubPackage().get())
                .persistencePath(ext.getPersistenceSubPackage().get())
                .implPath(ext.getImplSubPackage().get())
                .webAppPath(ext.getWebAppPath().get())
                .viewPath(ext.getViewPath().get())
                .viewExtension(ext.getViewExtension().get())
                .queryPath(ext.getQueryPath().get())
                .queryPrefix(ext.getQueryPrefix().get())
                .queryExt(ext.getQueryExt().get())
                .templateSet(ext.getTemplateSet().get())
                .customTemplateDir(customTplDir)
                .overwriteExisting(ext.getOverwriteExisting().get())
                .mapperType(ext.getMapperType().get())
                .controllerType(ext.getControllerType().get());
    }

    private ClassLoader buildJdbcClassLoader() {
        List<URL> urls = new ArrayList<>();
        for (File f : getJdbcClasspath().getFiles()) {
            try {
                urls.add(f.toURI().toURL());
            } catch (Exception e) {
                throw new GradleException("Invalid JDBC classpath entry: " + f, e);
            }
        }
        return new URLClassLoader(urls.toArray(new URL[0]),
                MvcGeneratorBaseTask.class.getClassLoader());
    }
}
