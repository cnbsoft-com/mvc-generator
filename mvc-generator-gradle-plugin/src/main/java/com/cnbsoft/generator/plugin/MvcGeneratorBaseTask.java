package com.cnbsoft.generator.plugin;

import com.cnbsoft.generator.engine.ColumnInspector;
import com.cnbsoft.generator.engine.GeneratorConfig;
import com.cnbsoft.generator.engine.TemplateEngine;
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
import java.util.ArrayList;
import java.util.List;

/**
 * 모든 MVC 코드 생성 태스크의 공통 베이스.
 *
 * - Extension 에서 GeneratorConfig 를 빌드
 * - JDBC 드라이버를 격리된 URLClassLoader 로 로드
 * - ColumnInspector / TemplateEngine 초기화 후 executeForTable() 위임
 */
public abstract class MvcGeneratorBaseTask extends DefaultTask {

    @Internal
    public abstract Property<MvcGeneratorExtension> getGeneratorExtension();

    @InputFiles
    public abstract ConfigurableFileCollection getJdbcClasspath();

    @TaskAction
    public final void execute() {
        MvcGeneratorExtension ext = getGeneratorExtension().get();
        GeneratorConfig config = buildConfig(ext);

        ClassLoader jdbcLoader = buildJdbcClassLoader();

        try (ColumnInspector inspector = new ColumnInspector(config, jdbcLoader)) {
            TemplateEngine engine = new TemplateEngine(config);
            for (String tableName : config.tableNames) {
                executeForTable(tableName, config, inspector, engine);
            }
        } catch (Exception e) {
            throw new GradleException("MVC generation failed: " + e.getMessage(), e);
        }
    }

    protected abstract void executeForTable(String tableName,
                                             GeneratorConfig config,
                                             ColumnInspector inspector,
                                             TemplateEngine engine) throws Exception;

    // ── 설정 빌드 ────────────────────────────────────────────────────

    private GeneratorConfig buildConfig(MvcGeneratorExtension ext) {
        File customTplDir = ext.getCustomTemplateDir().isPresent()
                ? ext.getCustomTemplateDir().get().getAsFile()
                : null;

        File outputDir = ext.getOutputDir().get().getAsFile();
        File resourceDir = ext.getResourceOutputDir().isPresent()
                ? ext.getResourceOutputDir().get().getAsFile()
                : outputDir;
        File viewDir = ext.getViewOutputDir().isPresent()
                ? ext.getViewOutputDir().get().getAsFile()
                : outputDir;

        return GeneratorConfig.builder()
                .dbDriver(ext.getDbDriver().get())
                .dbUrl(ext.getDbUrl().get())
                .dbUsername(ext.getDbUsername().get())
                .dbPassword(ext.getDbPassword().getOrElse(""))
                .tableNames(ext.getTableNames().get())
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
                .controllerType(ext.getControllerType().get())
                .modelSuffix(ext.getModelSuffix().get())
                .controllerSuffix(ext.getControllerSuffix().get())
                .serviceSuffix(ext.getServiceSuffix().get())
                .serviceImplSuffix(ext.getServiceImplSuffix().get())
                .mapperSuffix(ext.getMapperSuffix().get())
                .build();
    }

    // ── JDBC 드라이버 격리 ClassLoader ───────────────────────────────

    private ClassLoader buildJdbcClassLoader() {
        List<URL> urls = new ArrayList<>();
        for (File f : getJdbcClasspath().getFiles()) {
            try {
                urls.add(f.toURI().toURL());
            } catch (Exception e) {
                throw new GradleException("Invalid JDBC classpath entry: " + f, e);
            }
        }
        // 부모 ClassLoader는 플러그인의 ClassLoader: JDBC 클래스만 격리
        return new URLClassLoader(urls.toArray(new URL[0]),
                MvcGeneratorBaseTask.class.getClassLoader());
    }
}
