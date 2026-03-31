package com.cnbsoft.generator.plugin;

import com.cnbsoft.generator.task.*;
import org.gradle.api.Plugin;
import org.gradle.api.Project;
import org.gradle.api.artifacts.Configuration;
import org.gradle.api.tasks.TaskProvider;

public class MvcGeneratorPlugin implements Plugin<Project> {

    public static final String EXTENSION_NAME = "mvcGenerator";
    public static final String JDBC_CONFIG_NAME = "mvcGeneratorJdbc";
    public static final String TASK_GROUP = "MVC Generator";

    @Override
    public void apply(Project project) {

        // 1. DSL 확장 등록
        MvcGeneratorExtension ext = project.getExtensions()
                .create(EXTENSION_NAME, MvcGeneratorExtension.class, project.getObjects());

        // 2. JDBC 드라이버 전용 Configuration 등록
        Configuration jdbcConfig = project.getConfigurations().create(JDBC_CONFIG_NAME, c -> {
            c.setDescription("JDBC drivers for MVC code generation (not added to project classpath)");
            c.setVisible(true);
            c.setTransitive(false);
        });

        // 3. 개별 컴포넌트 태스크 등록
        TaskProvider<GenerateModelTask> modelTask = project.getTasks()
                .register("generateMvcModel", GenerateModelTask.class, t -> {
                    t.setGroup(TASK_GROUP);
                    t.setDescription("Generates the Model (VO) class from a database table");
                    t.getGeneratorExtension().set(ext);
                    t.getJdbcClasspath().setFrom(jdbcConfig);
                });

        TaskProvider<GenerateControllerTask> controllerTask = project.getTasks()
                .register("generateMvcController", GenerateControllerTask.class, t -> {
                    t.setGroup(TASK_GROUP);
                    t.setDescription("Generates the Spring MVC Controller class");
                    t.getGeneratorExtension().set(ext);
                    t.getJdbcClasspath().setFrom(jdbcConfig);
                });

        TaskProvider<GenerateServiceTask> serviceTask = project.getTasks()
                .register("generateMvcService", GenerateServiceTask.class, t -> {
                    t.setGroup(TASK_GROUP);
                    t.setDescription("Generates the Service interface and implementation");
                    t.getGeneratorExtension().set(ext);
                    t.getJdbcClasspath().setFrom(jdbcConfig);
                });

        TaskProvider<GeneratePersistenceTask> persistenceTask = project.getTasks()
                .register("generateMvcPersistence", GeneratePersistenceTask.class, t -> {
                    t.setGroup(TASK_GROUP);
                    t.setDescription("Generates the MyBatis Mapper interface");
                    t.getGeneratorExtension().set(ext);
                    t.getJdbcClasspath().setFrom(jdbcConfig);
                });

        TaskProvider<GenerateQueryTask> queryTask = project.getTasks()
                .register("generateMvcQuery", GenerateQueryTask.class, t -> {
                    t.setGroup(TASK_GROUP);
                    t.setDescription("Generates the MyBatis XML query mapper file");
                    t.getGeneratorExtension().set(ext);
                    t.getJdbcClasspath().setFrom(jdbcConfig);
                });

        TaskProvider<GenerateFormViewTask> formViewTask = project.getTasks()
                .register("generateMvcFormView", GenerateFormViewTask.class, t -> {
                    t.setGroup(TASK_GROUP);
                    t.setDescription("Generates the Create/Edit form view template");
                    t.getGeneratorExtension().set(ext);
                    t.getJdbcClasspath().setFrom(jdbcConfig);
                });

        TaskProvider<GenerateGetViewTask> getViewTask = project.getTasks()
                .register("generateMvcGetView", GenerateGetViewTask.class, t -> {
                    t.setGroup(TASK_GROUP);
                    t.setDescription("Generates the detail (get) view template");
                    t.getGeneratorExtension().set(ext);
                    t.getJdbcClasspath().setFrom(jdbcConfig);
                });

        TaskProvider<GenerateListViewTask> listViewTask = project.getTasks()
                .register("generateMvcListView", GenerateListViewTask.class, t -> {
                    t.setGroup(TASK_GROUP);
                    t.setDescription("Generates the list view template");
                    t.getGeneratorExtension().set(ext);
                    t.getJdbcClasspath().setFrom(jdbcConfig);
                });

        // 4. 대화형 태스크
        project.getTasks().register("generate",
                GenerateMvcInteractiveTask.class, t -> {
                    t.setGroup(TASK_GROUP);
                    t.setDescription(
                            "Interactively select tables and generation types. " +
                            "Run with: ./gradlew generate --no-daemon --console=plain");
                    t.getGeneratorExtension().set(ext);
                    t.getJdbcClasspath().setFrom(jdbcConfig);
                    t.getOutputs().upToDateWhen(task -> false);
                });

        // 5. 전체 집계 태스크
        project.getTasks().register("generateMvc", t -> {
            t.setGroup(TASK_GROUP);
            t.setDescription("Generates all MVC components (Model, Controller, Service, Persistence, Query, Views)");
            t.dependsOn(
                    modelTask, controllerTask, serviceTask,
                    persistenceTask, queryTask,
                    formViewTask, getViewTask, listViewTask
            );
        });
    }
}
