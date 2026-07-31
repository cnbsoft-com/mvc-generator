package com.cnbsoft.generator.plugin;

import org.gradle.testkit.runner.BuildResult;
import org.gradle.testkit.runner.GradleRunner;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import static org.junit.Assert.assertTrue;

/**
 * Gradle TestKit 기반 플러그인 적용 검증 테스트.
 * - 플러그인이 정상 적용되는지
 * - "MVC Generator" 그룹에 generate 태스크가 등록되는지
 * - 개별 컴포넌트 태스크 8개가 모두 등록되는지 (tasks --all 로 확인)
 */
public class PluginApplyTest {

    @Rule
    public final TemporaryFolder testProjectDir = new TemporaryFolder();

    @Test
    public void pluginAppliesAndRegistersGroupTask() throws IOException {
        writeProjectFiles();

        BuildResult result = GradleRunner.create()
                .withProjectDir(testProjectDir.getRoot())
                .withArguments("tasks", "--group", "MVC Generator")
                .withPluginClasspath()
                .build();

        String output = result.getOutput();
        assertTrue("generate task expected in 'MVC Generator' group", output.contains("generate "));
    }

    @Test
    public void pluginRegistersAllComponentTasks() throws IOException {
        writeProjectFiles();

        BuildResult result = GradleRunner.create()
                .withProjectDir(testProjectDir.getRoot())
                .withArguments("tasks", "--all")
                .withPluginClasspath()
                .build();

        String output = result.getOutput();
        assertTrue("generateMvc task expected",            output.contains("generateMvc "));
        assertTrue("generateMvcModel task expected",       output.contains("generateMvcModel"));
        assertTrue("generateMvcController task expected",  output.contains("generateMvcController"));
        assertTrue("generateMvcService task expected",     output.contains("generateMvcService"));
        assertTrue("generateMvcPersistence task expected", output.contains("generateMvcPersistence"));
        assertTrue("generateMvcQuery task expected",       output.contains("generateMvcQuery"));
        assertTrue("generateMvcFormView task expected",    output.contains("generateMvcFormView"));
        assertTrue("generateMvcGetView task expected",     output.contains("generateMvcGetView"));
        assertTrue("generateMvcListView task expected",    output.contains("generateMvcListView"));
    }

    private void writeProjectFiles() throws IOException {
        File settings = testProjectDir.newFile("settings.gradle");
        write(settings, "rootProject.name = 'test'\n");

        File buildFile = testProjectDir.newFile("build.gradle");
        write(buildFile,
                """
                        plugins { id 'com.cnbsoft.plugin.mvc-generator' }
                        mvcGenerator {
                            dbDriver   = 'com.mysql.cj.jdbc.Driver'
                            dbUrl      = 'jdbc:mysql://localhost:3306/test'
                            dbUsername = 'root'
                            tableNames = ['SAMPLE']
                            basePackage = 'com.cnbsoft.test'
                            outputDir = file('src/main')
                        }
                        """
        );
    }

    private void write(File file, String content) throws IOException {
        try (FileWriter fw = new FileWriter(file)) {
            fw.write(content);
        }
    }
}
