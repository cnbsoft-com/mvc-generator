package com.cnbsoft.generator.engine;

import com.cnbsoft.plugin.generator.util.StringUtil;

import java.io.File;

/**
 * 각 컴포넌트의 출력 파일 경로를 계산한다.
 * 기존 *GeneratorImpl 에 흩어져 있던 setFilePath() / makePath() 로직을 통합.
 */
public class PathResolver {

    // ── Java 소스 파일 ────────────────────────────────────────────────

    public static File modelFile(GeneratorConfig cfg, String tableName) {
        String javaName = StringUtil.tableNameToJavaName(tableName);
        return javaFile(cfg,
                cfg.modelPath,
                javaName + cfg.modelSuffix + ".java");
    }

    public static File controllerFile(GeneratorConfig cfg, String tableName) {
        String javaName = StringUtil.tableNameToJavaName(tableName);
        return javaFile(cfg,
                cfg.controllerPath,
                javaName + cfg.controllerSuffix + ".java");
    }

    public static File serviceInterfaceFile(GeneratorConfig cfg, String tableName) {
        String javaName = StringUtil.tableNameToJavaName(tableName);
        return javaFile(cfg,
                cfg.servicePath + File.separator + javaName.toLowerCase(),
                javaName + cfg.serviceSuffix + ".java");
    }

    public static File serviceImplFile(GeneratorConfig cfg, String tableName) {
        String javaName = StringUtil.tableNameToJavaName(tableName);
        return javaFile(cfg,
                cfg.servicePath + File.separator + javaName.toLowerCase() + File.separator + cfg.implPath,
                javaName + cfg.serviceImplSuffix + ".java");
    }

    public static File persistenceFile(GeneratorConfig cfg, String tableName) {
        String javaName = StringUtil.tableNameToJavaName(tableName);
        return javaFile(cfg,
                cfg.persistencePath + File.separator + javaName.toLowerCase(),
                javaName + cfg.mapperSuffix + ".java");
    }

    // ── 리소스 파일 ──────────────────────────────────────────────────

    public static File queryXmlFile(GeneratorConfig cfg, String tableName) {
        String fileName = cfg.queryPrefix
                + StringUtil.tableNameToJavaName(tableName)
                + "." + cfg.queryExt;
        return new File(cfg.resourceOutputDir,
                cfg.queryPath + File.separator + fileName);
    }

    // ── 뷰 파일 ─────────────────────────────────────────────────────

    public static File viewFile(GeneratorConfig cfg, String tableName, String templateName) {
        String dirName = tableName.toLowerCase().replaceAll("_", "");
        return new File(cfg.viewOutputDir,
                cfg.webAppPath
                + File.separator + join(cfg.viewPath)
                + File.separator + dirName
                + File.separator + templateName + cfg.viewExtension);
    }

    // ── 내부 헬퍼 ────────────────────────────────────────────────────

    /** basePackage를 경로로 변환 후 sub 패키지와 파일명을 조합 */
    private static File javaFile(GeneratorConfig cfg, String subPath, String fileName) {
        String packagePath = cfg.basePackage.replace('.', File.separatorChar);
        return new File(cfg.outputDir,
                "java" + File.separator
                + packagePath + File.separator
                + subPath + File.separator
                + fileName);
    }

    /** '/' 구분자를 OS 구분자로 변환 */
    private static String join(String path) {
        return path.replace("/", File.separator).replace("\\", File.separator);
    }
}
