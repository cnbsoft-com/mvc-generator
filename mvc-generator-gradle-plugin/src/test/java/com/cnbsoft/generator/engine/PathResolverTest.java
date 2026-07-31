package com.cnbsoft.generator.engine;

import org.junit.Test;

import java.io.File;
import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * PathResolver 의 계층별 구조(useDddPattern=false)와 도메인별 구조(useDddPattern=true)
 * 경로 조합 규칙을 검증한다.
 */
public class PathResolverTest {

    private static final File OUTPUT_DIR = new File("out");
    private static final File RESOURCE_DIR = new File("res");
    private static final File VIEW_DIR = new File("view");
    private static final String TABLE = "USER_DETAIL";

    private static GeneratorConfig config(boolean useDddPattern) {
        return GeneratorConfig.builder()
                .dbDriver("org.postgresql.Driver")
                .dbUrl("jdbc:postgresql://localhost/test")
                .dbUsername("test")
                .tableNames(List.of(TABLE))
                .basePackage("com.example.app")
                .outputDir(OUTPUT_DIR)
                .resourceOutputDir(RESOURCE_DIR)
                .viewOutputDir(VIEW_DIR)
                .useDddPattern(useDddPattern)
                .build();
    }

    /** '/'로 구분된 상대 경로를 OS 구분자로 변환해 root 기준 File을 만든다. */
    private static File resolve(File root, String unixRelativePath) {
        return new File(root, unixRelativePath.replace("/", File.separator));
    }

    @Test
    public void modelFileLayeredStructure() {
        assertEquals(resolve(OUTPUT_DIR, "java/com/example/app/vo/UserDetail.java"),
                PathResolver.modelFile(config(false), TABLE));
    }

    @Test
    public void modelFileDomainStructure() {
        assertEquals(resolve(OUTPUT_DIR, "java/com/example/app/userdetail/vo/UserDetail.java"),
                PathResolver.modelFile(config(true), TABLE));
    }

    @Test
    public void controllerFileLayeredStructure() {
        assertEquals(resolve(OUTPUT_DIR, "java/com/example/app/controller/UserDetailController.java"),
                PathResolver.controllerFile(config(false), TABLE));
    }

    @Test
    public void controllerFileDomainStructure() {
        assertEquals(resolve(OUTPUT_DIR, "java/com/example/app/userdetail/controller/UserDetailController.java"),
                PathResolver.controllerFile(config(true), TABLE));
    }

    @Test
    public void serviceInterfaceFileLayeredStructure() {
        assertEquals(resolve(OUTPUT_DIR, "java/com/example/app/service/userdetail/UserDetailService.java"),
                PathResolver.serviceInterfaceFile(config(false), TABLE));
    }

    @Test
    public void serviceInterfaceFileDomainStructure() {
        assertEquals(resolve(OUTPUT_DIR, "java/com/example/app/userdetail/service/UserDetailService.java"),
                PathResolver.serviceInterfaceFile(config(true), TABLE));
    }

    @Test
    public void serviceImplFileLayeredStructure() {
        assertEquals(resolve(OUTPUT_DIR, "java/com/example/app/service/userdetail/impl/UserDetailServiceImpl.java"),
                PathResolver.serviceImplFile(config(false), TABLE));
    }

    @Test
    public void serviceImplFileDomainStructure() {
        assertEquals(resolve(OUTPUT_DIR, "java/com/example/app/userdetail/service/impl/UserDetailServiceImpl.java"),
                PathResolver.serviceImplFile(config(true), TABLE));
    }

    @Test
    public void persistenceFileLayeredStructure() {
        assertEquals(resolve(OUTPUT_DIR, "java/com/example/app/mapper/userdetail/UserDetailMapper.java"),
                PathResolver.persistenceFile(config(false), TABLE));
    }

    @Test
    public void persistenceFileDomainStructure() {
        assertEquals(resolve(OUTPUT_DIR, "java/com/example/app/userdetail/mapper/UserDetailMapper.java"),
                PathResolver.persistenceFile(config(true), TABLE));
    }

    @Test
    public void queryXmlFileLayeredStructure() {
        assertEquals(resolve(RESOURCE_DIR, "mapper/mapper-UserDetail.xml"),
                PathResolver.queryXmlFile(config(false), TABLE));
    }

    @Test
    public void queryXmlFileDomainStructure() {
        assertEquals(resolve(RESOURCE_DIR, "mapper/userdetail/mapper-UserDetail.xml"),
                PathResolver.queryXmlFile(config(true), TABLE));
    }

    @Test
    public void viewFileLayeredStructure() {
        assertEquals(resolve(VIEW_DIR, "webapp/WEB-INF/views/userdetail/user_get.tpl"),
                PathResolver.viewFile(config(false), TABLE, "user_get"));
    }

    @Test
    public void viewFileDomainStructure() {
        assertEquals(resolve(VIEW_DIR, "webapp/WEB-INF/views/userdetail/user_get.tpl"),
                PathResolver.viewFile(config(true), TABLE, "user_get"));
    }
}
