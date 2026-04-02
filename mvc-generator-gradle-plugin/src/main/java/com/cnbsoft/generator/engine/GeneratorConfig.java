package com.cnbsoft.generator.engine;

import java.io.File;
import java.util.List;

/**
 * 코드 생성에 필요한 모든 설정을 담는 불변 POJO.
 * 기존 generate.properties + gendatasource.properties + 인터랙티브 CLI 입력을
 * 모두 대체한다.
 */
public class GeneratorConfig {

    // ── DB 접속 ──────────────────────────────────────────────────────
    public final String dbDriver;
    public final String dbUrl;
    public final String dbUsername;
    public final String dbPassword;

    // ── 생성 대상 테이블 ─────────────────────────────────────────────
    public final List<String> tableNames;

    // ── 출력 루트 경로 ───────────────────────────────────────────────
    public final File outputDir;          // Java 소스 루트 (src/main/java)
    public final File resourceOutputDir;  // 리소스 루트 (src/main/resources)
    public final File viewOutputDir;      // 웹앱 루트 (src/main/webapp)

    // ── 패키지 구조 ──────────────────────────────────────────────────
    public final String basePackage;
    public final String modelPath;
    public final String controllerPath;
    public final String servicePath;
    public final String persistencePath;
    public final String implPath;

    // ── 뷰 설정 ─────────────────────────────────────────────────────
    public final String webAppPath;
    public final String viewPath;
    public final String viewExtension;

    // ── MyBatis 쿼리 파일 설정 ───────────────────────────────────────
    public final String queryPath;
    public final String queryPrefix;
    public final String queryExt;

    // ── 템플릿 설정 ──────────────────────────────────────────────────
    public final String templateSet;
    public final File customTemplateDir;  // null 이면 기본 내장 템플릿 사용

    // ── 동작 옵션 ────────────────────────────────────────────────────
    public final boolean overwriteExisting;

    // ── Mapper 유형 ("xml" | "annotation") ──────────────────────────
    public final String mapperType;

    // ── Controller 유형 ("web" | "api") ─────────────────────────────
    public final String controllerType;

    // ── 각 계층 클래스명 접미사 ─────────────────────────────────────
    public final String modelSuffix;
    public final String controllerSuffix;
    public final String serviceSuffix;
    public final String serviceImplSuffix;
    public final String mapperSuffix;

    // ── 템플릿 파일명 (generate.properties 의 키와 동일) ─────────────
    public final String tplModel         = "vo";
    public final String tplController    = "controller";
    public final String tplService       = "service";
    public final String tplServiceImpl   = "service_impl";
    public final String tplPersistence      = "persistence";
    public final String tplPersistenceAnno  = "persistence_anno";
    public final String tplQuery            = "query";
    public final String tplForm          = "form";
    public final String tplGet           = "get";
    public final String tplList          = "list";
    public final String tplExtension     = "tpl";

    private GeneratorConfig(Builder b) {
        this.dbDriver           = b.dbDriver;
        this.dbUrl              = b.dbUrl;
        this.dbUsername         = b.dbUsername;
        this.dbPassword         = b.dbPassword;
        this.tableNames         = b.tableNames;
        this.outputDir          = b.outputDir;
        this.resourceOutputDir  = b.resourceOutputDir;
        this.viewOutputDir      = b.viewOutputDir;
        this.basePackage        = b.basePackage;
        this.modelPath          = b.modelPath;
        this.controllerPath     = b.controllerPath;
        this.servicePath        = b.servicePath;
        this.persistencePath    = b.persistencePath;
        this.implPath           = b.implPath;
        this.webAppPath         = b.webAppPath;
        this.viewPath           = b.viewPath;
        this.viewExtension      = b.viewExtension;
        this.queryPath          = b.queryPath;
        this.queryPrefix        = b.queryPrefix;
        this.queryExt           = b.queryExt;
        this.templateSet        = b.templateSet;
        this.customTemplateDir  = b.customTemplateDir;
        this.overwriteExisting  = b.overwriteExisting;
        this.mapperType         = b.mapperType;
        this.controllerType     = b.controllerType;
        this.modelSuffix        = b.modelSuffix;
        this.controllerSuffix   = b.controllerSuffix;
        this.serviceSuffix      = b.serviceSuffix;
        this.serviceImplSuffix  = b.serviceImplSuffix;
        this.mapperSuffix       = b.mapperSuffix;
    }

    public static Builder builder() { return new Builder(); }

    public static class Builder {
        private String dbDriver;
        private String dbUrl;
        private String dbUsername;
        private String dbPassword;
        private List<String> tableNames;
        private File outputDir;
        private File resourceOutputDir;
        private File viewOutputDir;
        private String basePackage;
        private String modelPath        = "vo";
        private String controllerPath   = "controller";
        private String servicePath      = "service";
        private String persistencePath  = "mapper";
        private String implPath         = "impl";
        private String webAppPath       = "webapp";
        private String viewPath         = "WEB-INF/views";
        private String viewExtension    = ".tpl";
        private String queryPath        = "mapper";
        private String queryPrefix      = "mapper-";
        private String queryExt         = "xml";
        private String templateSet      = "default";
        private File customTemplateDir  = null;
        private boolean overwriteExisting  = false;
        private String mapperType          = "xml";
        private String controllerType      = "web";
        private String modelSuffix         = "";
        private String controllerSuffix    = "Controller";
        private String serviceSuffix       = "Service";
        private String serviceImplSuffix   = "ServiceImpl";
        private String mapperSuffix        = "Mapper";

        public Builder dbDriver(String v)          { this.dbDriver = v; return this; }
        public Builder dbUrl(String v)             { this.dbUrl = v; return this; }
        public Builder dbUsername(String v)        { this.dbUsername = v; return this; }
        public Builder dbPassword(String v)        { this.dbPassword = v; return this; }
        public Builder tableNames(List<String> v)  { this.tableNames = v; return this; }
        public Builder outputDir(File v)           { this.outputDir = v; return this; }
        public Builder resourceOutputDir(File v)   { this.resourceOutputDir = v; return this; }
        public Builder viewOutputDir(File v)       { this.viewOutputDir = v; return this; }
        public Builder basePackage(String v)       { this.basePackage = v; return this; }
        public Builder modelPath(String v)         { this.modelPath = v; return this; }
        public Builder controllerPath(String v)    { this.controllerPath = v; return this; }
        public Builder servicePath(String v)       { this.servicePath = v; return this; }
        public Builder persistencePath(String v)   { this.persistencePath = v; return this; }
        public Builder implPath(String v)          { this.implPath = v; return this; }
        public Builder webAppPath(String v)        { this.webAppPath = v; return this; }
        public Builder viewPath(String v)          { this.viewPath = v; return this; }
        public Builder viewExtension(String v)     { this.viewExtension = v; return this; }
        public Builder queryPath(String v)         { this.queryPath = v; return this; }
        public Builder queryPrefix(String v)       { this.queryPrefix = v; return this; }
        public Builder queryExt(String v)          { this.queryExt = v; return this; }
        public Builder templateSet(String v)       { this.templateSet = v; return this; }
        public Builder customTemplateDir(File v)   { this.customTemplateDir = v; return this; }
        public Builder overwriteExisting(boolean v){ this.overwriteExisting = v; return this; }
        public Builder mapperType(String v)          { this.mapperType = v; return this; }
        public Builder controllerType(String v)      { this.controllerType = v; return this; }
        public Builder modelSuffix(String v)         { this.modelSuffix = v; return this; }
        public Builder controllerSuffix(String v)    { this.controllerSuffix = v; return this; }
        public Builder serviceSuffix(String v)       { this.serviceSuffix = v; return this; }
        public Builder serviceImplSuffix(String v)   { this.serviceImplSuffix = v; return this; }
        public Builder mapperSuffix(String v)        { this.mapperSuffix = v; return this; }

        public GeneratorConfig build() {
            if (dbDriver == null || dbUrl == null || dbUsername == null) {
                throw new IllegalStateException("dbDriver, dbUrl, dbUsername are required");
            }
            if (tableNames == null || tableNames.isEmpty()) {
                throw new IllegalStateException("tableNames must not be empty");
            }
            if (basePackage == null) {
                throw new IllegalStateException("basePackage is required");
            }
            if (outputDir == null) {
                throw new IllegalStateException("outputDir is required");
            }
            if (resourceOutputDir == null) {
                resourceOutputDir = outputDir;
            }
            if (viewOutputDir == null) {
                viewOutputDir = outputDir;
            }
            return new GeneratorConfig(this);
        }

        /** Interactive 태스크 전용: tableNames 검증 없이 빌드 (DB 연결 전용 config) */
        public GeneratorConfig buildSkipTableCheck() {
            if (dbDriver == null || dbUrl == null || dbUsername == null) {
                throw new IllegalStateException("dbDriver, dbUrl, dbUsername are required");
            }
            if (basePackage == null) {
                throw new IllegalStateException("basePackage is required");
            }
            if (outputDir == null) {
                throw new IllegalStateException("outputDir is required");
            }
            if (resourceOutputDir == null) resourceOutputDir = outputDir;
            if (viewOutputDir == null)     viewOutputDir     = outputDir;
            return new GeneratorConfig(this);
        }
    }
}
