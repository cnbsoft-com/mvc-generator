package com.cnbsoft.plugin.generator.task;

import com.cnbsoft.plugin.generator.engine.ColumnInspector;
import com.cnbsoft.plugin.generator.engine.GeneratorConfig;
import com.cnbsoft.plugin.generator.engine.TemplateEngine;
import com.cnbsoft.plugin.generator.engine.generators.PersistenceCodeGenerator;
import com.cnbsoft.plugin.generator.plugin.MvcGeneratorBaseTask;

public abstract class GeneratePersistenceTask extends MvcGeneratorBaseTask {
    @Override
    protected void executeForTable(String tableName, GeneratorConfig config,
                                    ColumnInspector inspector, TemplateEngine engine) throws Exception {
        new PersistenceCodeGenerator(config, inspector, engine).generate(tableName);
    }
}
