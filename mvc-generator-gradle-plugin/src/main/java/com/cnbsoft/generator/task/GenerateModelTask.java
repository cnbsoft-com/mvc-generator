package com.cnbsoft.generator.task;

import com.cnbsoft.generator.engine.ColumnInspector;
import com.cnbsoft.generator.engine.GeneratorConfig;
import com.cnbsoft.generator.engine.TemplateEngine;
import com.cnbsoft.generator.engine.generators.ModelCodeGenerator;
import com.cnbsoft.generator.plugin.MvcGeneratorBaseTask;

public abstract class GenerateModelTask extends MvcGeneratorBaseTask {
    @Override
    protected void executeForTable(String tableName, GeneratorConfig config,
                                    ColumnInspector inspector, TemplateEngine engine) throws Exception {
        new ModelCodeGenerator(config, inspector, engine).generate(tableName);
    }
}
