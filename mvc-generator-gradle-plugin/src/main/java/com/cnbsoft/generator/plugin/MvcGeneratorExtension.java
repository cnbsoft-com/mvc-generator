package com.cnbsoft.generator.plugin;

import org.gradle.api.file.DirectoryProperty;
import org.gradle.api.model.ObjectFactory;
import org.gradle.api.provider.ListProperty;
import org.gradle.api.provider.Property;

import javax.inject.Inject;

/**
 * build.gradle 의 mvcGenerator { } 블록에서 사용할 수 있는 Gradle DSL 확장.
 * 기존 generate.properties + gendatasource.properties + 인터랙티브 CLI 입력을 모두 대체한다.
 *
 * 사용 예:
 * <pre>
 * mvcGenerator {
 *     dbDriver   = 'com.mysql.cj.jdbc.Driver'
 *     dbUrl      = 'jdbc:mysql://localhost:3306/mydb'
 *     dbUsername = 'root'
 *     dbPassword = project.findProperty('dbPassword') ?: ''
 *
 *     tableNames = ['USER_ACCOUNT', 'PRODUCT_ITEM']
 *     basePackage = 'kr.co.myapp.web'
 *
 *     outputDir         = file("src/main/java")
 *     resourceOutputDir = file("src/main/resources")
 *     viewOutputDir     = file("src/main/webapp")
 * }
 * </pre>
 */
public class MvcGeneratorExtension {

    // ── DB 접속 ──────────────────────────────────────────────────────
    private final Property<String> dbDriver;
    private final Property<String> dbUrl;
    private final Property<String> dbUsername;
    private final Property<String> dbPassword;

    // ── 생성 대상 테이블 ─────────────────────────────────────────────
    private final ListProperty<String> tableNames;

    // ── 출력 경로 ────────────────────────────────────────────────────
    private final DirectoryProperty outputDir;
    private final DirectoryProperty resourceOutputDir;
    private final DirectoryProperty viewOutputDir;

    // ── 패키지 구조 ──────────────────────────────────────────────────
    private final Property<String> basePackage;
    private final Property<String> modelSubPackage;
    private final Property<String> controllerSubPackage;
    private final Property<String> serviceSubPackage;
    private final Property<String> persistenceSubPackage;
    private final Property<String> implSubPackage;

    // ── 뷰 설정 ─────────────────────────────────────────────────────
    private final Property<String> webAppPath;
    private final Property<String> viewPath;
    private final Property<String> viewExtension;

    // ── MyBatis 쿼리 설정 ────────────────────────────────────────────
    private final Property<String> queryPath;
    private final Property<String> queryPrefix;
    private final Property<String> queryExt;

    // ── 템플릿 설정 ──────────────────────────────────────────────────
    private final Property<String> templateSet;
    private final DirectoryProperty customTemplateDir;

    // ── 동작 옵션 ────────────────────────────────────────────────────
    private final Property<Boolean> overwriteExisting;

    // ── Mapper 유형 ──────────────────────────────────────────────────
    // "xml"        : XML 기반 MyBatis Mapper (기본값)
    // "annotation" : Annotation 기반 MyBatis Mapper
    private final Property<String> mapperType;

    // ── Controller 유형 ──────────────────────────────────────────────
    // "web" : @Controller (기본값, View 반환)
    // "api" : @RestController (JSON 반환)
    private final Property<String> controllerType;

    // ── 각 계층 클래스명 접미사 ──────────────────────────────────────
    private final Property<String> modelSuffix;
    private final Property<String> controllerSuffix;
    private final Property<String> serviceSuffix;
    private final Property<String> serviceImplSuffix;
    private final Property<String> mapperSuffix;

    @Inject
    public MvcGeneratorExtension(ObjectFactory objects) {
        dbDriver            = objects.property(String.class);
        dbUrl               = objects.property(String.class);
        dbUsername          = objects.property(String.class);
        dbPassword          = objects.property(String.class).convention("");
        tableNames          = objects.listProperty(String.class);
        outputDir           = objects.directoryProperty();
        resourceOutputDir   = objects.directoryProperty();
        viewOutputDir       = objects.directoryProperty();
        basePackage         = objects.property(String.class);
        modelSubPackage     = objects.property(String.class).convention("vo");
        controllerSubPackage = objects.property(String.class).convention("controller");
        serviceSubPackage   = objects.property(String.class).convention("service");
        persistenceSubPackage = objects.property(String.class).convention("mapper");
        implSubPackage      = objects.property(String.class).convention("impl");
        webAppPath          = objects.property(String.class).convention("webapp");
        viewPath            = objects.property(String.class).convention("WEB-INF/views");
        viewExtension       = objects.property(String.class).convention(".tpl");
        queryPath           = objects.property(String.class).convention("mapper");
        queryPrefix         = objects.property(String.class).convention("mapper-");
        queryExt            = objects.property(String.class).convention("xml");
        templateSet         = objects.property(String.class).convention("default");
        customTemplateDir   = objects.directoryProperty();
        overwriteExisting   = objects.property(Boolean.class).convention(false);
        mapperType          = objects.property(String.class).convention("xml");
        controllerType      = objects.property(String.class).convention("web");
        modelSuffix         = objects.property(String.class).convention("");
        controllerSuffix    = objects.property(String.class).convention("Controller");
        serviceSuffix       = objects.property(String.class).convention("Service");
        serviceImplSuffix   = objects.property(String.class).convention("ServiceImpl");
        mapperSuffix        = objects.property(String.class).convention("Mapper");
    }

    public Property<String> getDbDriver()              { return dbDriver; }
    public Property<String> getDbUrl()                 { return dbUrl; }
    public Property<String> getDbUsername()            { return dbUsername; }
    public Property<String> getDbPassword()            { return dbPassword; }
    public ListProperty<String> getTableNames()        { return tableNames; }
    public DirectoryProperty getOutputDir()            { return outputDir; }
    public DirectoryProperty getResourceOutputDir()    { return resourceOutputDir; }
    public DirectoryProperty getViewOutputDir()        { return viewOutputDir; }
    public Property<String> getBasePackage()           { return basePackage; }
    public Property<String> getModelSubPackage()       { return modelSubPackage; }
    public Property<String> getControllerSubPackage()  { return controllerSubPackage; }
    public Property<String> getServiceSubPackage()     { return serviceSubPackage; }
    public Property<String> getPersistenceSubPackage() { return persistenceSubPackage; }
    public Property<String> getImplSubPackage()        { return implSubPackage; }
    public Property<String> getWebAppPath()            { return webAppPath; }
    public Property<String> getViewPath()              { return viewPath; }
    public Property<String> getViewExtension()         { return viewExtension; }
    public Property<String> getQueryPath()             { return queryPath; }
    public Property<String> getQueryPrefix()           { return queryPrefix; }
    public Property<String> getQueryExt()              { return queryExt; }
    public Property<String> getTemplateSet()           { return templateSet; }
    public DirectoryProperty getCustomTemplateDir()    { return customTemplateDir; }
    public Property<Boolean> getOverwriteExisting()    { return overwriteExisting; }
    public Property<String>  getMapperType()             { return mapperType; }
    public Property<String>  getControllerType()         { return controllerType; }
    public Property<String>  getModelSuffix()            { return modelSuffix; }
    public Property<String>  getControllerSuffix()       { return controllerSuffix; }
    public Property<String>  getServiceSuffix()          { return serviceSuffix; }
    public Property<String>  getServiceImplSuffix()      { return serviceImplSuffix; }
    public Property<String>  getMapperSuffix()           { return mapperSuffix; }
}
