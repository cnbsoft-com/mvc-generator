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
        GeneratorConfig config = ext.toConfigBuilder()
                .tableNames(ext.getTableNames().get())
                .build();

        ClassLoader jdbcLoader = buildJdbcClassLoader(getJdbcClasspath());

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

    // ── JDBC 드라이버 격리 ClassLoader ───────────────────────────────

    /** JDBC 드라이버 전용 격리 ClassLoader 생성. Task 종류와 무관하게 공유되는 로직. */
    public static ClassLoader buildJdbcClassLoader(ConfigurableFileCollection jdbcClasspath) {
        List<URL> urls = new ArrayList<>();
        for (File f : jdbcClasspath.getFiles()) {
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
