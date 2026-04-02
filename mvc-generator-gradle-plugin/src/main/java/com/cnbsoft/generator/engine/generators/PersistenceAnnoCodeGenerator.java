package com.cnbsoft.generator.engine.generators;

import com.cnbsoft.generator.engine.ColumnInspector;
import com.cnbsoft.generator.engine.GeneratorConfig;
import com.cnbsoft.generator.engine.PathResolver;
import com.cnbsoft.generator.engine.TemplateEngine;
import com.cnbsoft.plugin.generator.util.StringUtil;
import com.cnbsoft.plugin.generator.vo.ColumnInfo;
import com.cnbsoft.plugin.generator.vo.PrimaryInfo;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * MyBatis Annotation 방식 Mapper 인터페이스 생성기.
 * XML 쿼리 파일 없이 @Select / @Insert / @Update / @Delete 어노테이션으로 SQL을 관리한다.
 * persistence_anno.tpl 템플릿을 사용하며 persistenceFile() 경로에 파일을 생성한다.
 */
public class PersistenceAnnoCodeGenerator {

    private final GeneratorConfig config;
    private final ColumnInspector inspector;
    private final TemplateEngine engine;

    public PersistenceAnnoCodeGenerator(GeneratorConfig config, ColumnInspector inspector, TemplateEngine engine) {
        this.config   = config;
        this.inspector = inspector;
        this.engine   = engine;
    }

    public void generate(String tableName) throws Exception {
        List<ColumnInfo> columns      = inspector.getColumnInfos(tableName);
        List<PrimaryInfo> primaryInfos = inspector.getPrimaryInfo(tableName);

        Map<String, Object> model = new HashMap<>();
        model.put("tableName",      tableName);
        model.put("columns",        columns);
        model.put("primaryColumns", primaryInfos);
        model.put("modelName",      StringUtil.tableNameToJavaName(tableName).toLowerCase());
        model.put("packagePath",    config.basePackage);
        model.put("modelPath",      config.modelPath);
        model.put("servicePath",    config.servicePath);
        model.put("persistencePath", config.persistencePath);
        model.put("implPath",       config.implPath);
        model.put("modelSuffix",    config.modelSuffix);
        model.put("controllerSuffix", config.controllerSuffix);
        model.put("serviceSuffix",  config.serviceSuffix);
        model.put("serviceImplSuffix", config.serviceImplSuffix);
        model.put("mapperSuffix",   config.mapperSuffix);

        File outFile = PathResolver.persistenceFile(config, tableName);
        engine.generateFile(outFile, config.tplPersistenceAnno, model);
    }
}
