package com.cnbsoft.generator.engine;

import freemarker.cache.ClassTemplateLoader;
import freemarker.cache.FileTemplateLoader;
import freemarker.cache.MultiTemplateLoader;
import freemarker.cache.TemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

import java.io.*;
import java.util.Map;

/**
 * FreeMarker 템플릿 엔진 래퍼.
 * 기존 AbstractGenerator 의 createFile() / freemarkerConfiguration 설정 로직을 대체한다.
 *
 * 템플릿 해석 우선순위:
 *   1. customTemplateDir/<templateSet>/<name>.tpl  (사용자 파일시스템 오버라이드)
 *   2. classpath:/com/cnbsoft/generator/templates/<templateSet>/<name>.tpl  (플러그인 내장)
 */
public class TemplateEngine {

    private final Configuration freemarkerConfig;
    private final GeneratorConfig config;

    public TemplateEngine(GeneratorConfig config) throws IOException {
        this.config = config;

        freemarkerConfig = new Configuration(Configuration.VERSION_2_3_32);
        freemarkerConfig.setDefaultEncoding("UTF-8");
        freemarkerConfig.setDateFormat("yyyy-MM-dd");
        freemarkerConfig.setDateTimeFormat("yyyy-MM-dd HH:mm:ss");
        freemarkerConfig.setWhitespaceStripping(true);

        String classpathBase = "/com/cnbsoft/generator/templates/"
                + config.templateSet + "/";
        ClassTemplateLoader classpathLoader =
                new ClassTemplateLoader(TemplateEngine.class, classpathBase);

        if (config.customTemplateDir != null && config.customTemplateDir.exists()) {
            File customSet = new File(config.customTemplateDir, config.templateSet);
            File effectiveCustomDir = customSet.exists() ? customSet : config.customTemplateDir;
            FileTemplateLoader customLoader = new FileTemplateLoader(effectiveCustomDir);
            freemarkerConfig.setTemplateLoader(
                    new MultiTemplateLoader(new TemplateLoader[]{customLoader, classpathLoader})
            );
        } else {
            freemarkerConfig.setTemplateLoader(classpathLoader);
        }
    }

    /**
     * 템플릿을 처리하여 대상 파일에 출력한다.
     *
     * @param targetFile   생성할 파일 (디렉토리가 없으면 자동 생성)
     * @param templateName 확장자 없는 템플릿 이름 (e.g. "vo", "controller")
     * @param model        FreeMarker 모델 맵
     */
    public void generateFile(File targetFile, String templateName, Map<String, Object> model)
            throws IOException, TemplateException {

        if (!config.overwriteExisting && targetFile.exists()) {
            System.out.println("SKIP (already exists): " + targetFile.getPath());
            return;
        }

        targetFile.getParentFile().mkdirs();

        Template template = freemarkerConfig.getTemplate(
                templateName + "." + config.tplExtension);

        try (Writer writer = new BufferedWriter(new FileWriter(targetFile))) {
            template.process(model, writer);
        }

        System.out.println("GENERATED: " + targetFile.getPath());
    }
}
