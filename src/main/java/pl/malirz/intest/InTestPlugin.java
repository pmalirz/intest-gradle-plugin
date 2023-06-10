package pl.malirz.intest;

import java.util.Arrays;
import org.gradle.api.Plugin;
import org.gradle.api.Project;
import org.gradle.api.plugins.GroovyPlugin;
import org.gradle.api.plugins.JavaPlugin;
import org.gradle.api.tasks.SourceSet;
import org.gradle.api.tasks.SourceSetContainer;
import org.gradle.api.tasks.testing.Test;

public class InTestPlugin implements Plugin<Project> {
    @Override
    public void apply(Project project) {
        final InTestExtension intest =
            project.getExtensions().create("intest", InTestExtension.class);

        project.getConfigurations().create("intestImplementation").extendsFrom(
            project.getConfigurations().getByName("implementation"),
            project.getConfigurations().getByName("testImplementation")
        );

        project.getConfigurations().create("intestRuntimeOnly").extendsFrom(
            project.getConfigurations().getByName("runtimeOnly")
        );

        final SourceSetContainer sourceSets =
            project.getExtensions().getByType(SourceSetContainer.class);
        final SourceSet intestSrc = sourceSets.create("intest");

        intestSrc.setCompileClasspath(
            intestSrc.getCompileClasspath()
                .plus(sourceSets.getByName("main").getOutput())
        );
        intestSrc.setRuntimeClasspath(
            intestSrc.getRuntimeClasspath()
                .plus(sourceSets.getByName("main").getOutput())
        );

        intestSrc.getResources().srcDir("src/intest/resources");
        intestSrc.getAllSource().srcDir("src/intest/java");

        project.getPlugins().withType(JavaPlugin.class, javaPlugin -> {
            intestSrc.getAllSource().srcDir("src/intest/java");
        });
        project.getPlugins().withType(GroovyPlugin.class, groovyPlugin -> {
            intestSrc.getAllSource().srcDir("src/intest/groovy");
        });
        project.getPlugins().withId("org.jetbrains.kotlin.jvm", kotlinJvmPlugin -> {
            intestSrc.getAllSource().srcDir("src/intest/kotlin");
        });

        project.getTasks().register("intest", Test.class, task -> {
            task.setGroup("verification");
            task.setDescription("Runs integration tests.");
            task.setTestClassesDirs(intestSrc.getOutput().getClassesDirs());
            task.setClasspath(intestSrc.getRuntimeClasspath());
            task.setShouldRunAfter(Arrays.asList("test"));
        });
    }
}
