package com.cnbsoft.plugin.generator;

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
 * - 9개 태스크가 "MVC Generator" 그룹에 등록되는지
 */
public class PluginApplyTest {

    @Rule
    public final TemporaryFolder testProjectDir = new TemporaryFolder();

    @Test
    public void pluginAppliesAndRegistersAllTasks() throws IOException {
        // settings.gradle
        File settings = testProjectDir.newFile("settings.gradle");
        write(settings, "rootProject.name = 'test'\n");

        // build.gradle — 실제 DB 연결 없이 tasks 목록만 확인
        File buildFile = testProjectDir.newFile("build.gradle");
        write(buildFile,
                "plugins { id 'com.cnbsoft.mvc-generator' }\n" +
                "mvcGenerator {\n" +
                "    dbDriver   = 'com.mysql.cj.jdbc.Driver'\n" +
                "    dbUrl      = 'jdbc:mysql://localhost:3306/test'\n" +
                "    dbUsername = 'root'\n" +
                "    tableNames = ['SAMPLE']\n" +
                "    basePackage = 'com.cnbsoft.test'\n" +
                "    outputDir = file('src/main')\n" +
                "}\n"
        );

        BuildResult result = GradleRunner.create()
                .withProjectDir(testProjectDir.getRoot())
                .withArguments("tasks", "--group", "MVC Generator")
                .withPluginClasspath()
                .build();

        String output = result.getOutput();
        assertTrue("generateMvc task expected",            output.contains("generateMvc "));
        assertTrue("generateMvcModel task expected",       output.contains("generateMvcModel"));
        assertTrue("generateMvcController task expected",  output.contains("generateMvcController"));
        assertTrue("generateMvcService task expected",     output.contains("generateMvcService"));
        assertTrue("generateMvcPersistence task expected", output.contains("generateMvcPersistence"));
        assertTrue("generateMvcMapper task expected",       output.contains("generateMvcMapper"));
        assertTrue("generateMvcFormView task expected",    output.contains("generateMvcFormView"));
        assertTrue("generateMvcGetView task expected",     output.contains("generateMvcGetView"));
        assertTrue("generateMvcListView task expected",    output.contains("generateMvcListView"));
    }

    private void write(File file, String content) throws IOException {
        try (FileWriter fw = new FileWriter(file)) {
            fw.write(content);
        }
    }
}
