package com.cnbsoft.generator.task;

import com.cnbsoft.generator.engine.ColumnInspector;
import com.cnbsoft.generator.engine.GeneratorConfig;
import com.cnbsoft.generator.engine.TemplateEngine;
import com.cnbsoft.generator.engine.generators.QueryCodeGenerator;
import com.cnbsoft.generator.plugin.MvcGeneratorBaseTask;

public abstract class GenerateQueryTask extends MvcGeneratorBaseTask {
    @Override
    protected void executeForTable(String tableName, GeneratorConfig config,
                                    ColumnInspector inspector, TemplateEngine engine) throws Exception {
        new QueryCodeGenerator(config, inspector, engine).generate(tableName);
    }
}
