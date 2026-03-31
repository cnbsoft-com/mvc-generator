package com.cnbsoft.generator.task;

import com.cnbsoft.generator.engine.ColumnInspector;
import com.cnbsoft.generator.engine.GeneratorConfig;
import com.cnbsoft.generator.engine.TemplateEngine;
import com.cnbsoft.generator.engine.generators.ControllerCodeGenerator;
import com.cnbsoft.generator.plugin.MvcGeneratorBaseTask;

public abstract class GenerateControllerTask extends MvcGeneratorBaseTask {
    @Override
    protected void executeForTable(String tableName, GeneratorConfig config,
                                    ColumnInspector inspector, TemplateEngine engine) throws Exception {
        new ControllerCodeGenerator(config, inspector, engine).generate(tableName);
    }
}
