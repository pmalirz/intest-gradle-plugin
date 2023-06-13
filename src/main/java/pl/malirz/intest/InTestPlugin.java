package pl.malirz.intest;

import org.gradle.api.Plugin;
import org.gradle.api.Project;
import org.gradle.api.plugins.GroovyPlugin;
import org.gradle.api.plugins.JavaPlugin;
import org.gradle.api.tasks.SourceSet;
import org.gradle.api.tasks.SourceSetContainer;
import org.gradle.api.tasks.testing.Test;
import org.jetbrains.annotations.NotNull;

import java.util.Set;

/**
 * Plugin that adds integration tests phase to a project. <br>
 * The <b>intest</b> task is added to the project as well. <br>
 * Although the <b>intest</b> task should run after the <b>test</b> task it does not run automatically by default.
 * To run the <b>intest</b> task use the following command: <code>gradlew intest</code>.
 * <br>
 * Standard configuration of the plugin is as follows:
 * <pre>
 * plugins {
 *     id 'java-library'
 *     id 'pl.malirz.intest'
 * }
 *
 * intest {
 *     useJUnitPlatform()
 * }
 * </pre>
 */
public class InTestPlugin implements Plugin<Project> {

    public static final String SRC_INTEST_JAVA = "src/intest/java";
    public static final String SRC_INTEST_GROOVY = "src/intest/groovy";
    public static final String SRC_INTEST_KOTLIN = "src/intest/kotlin";
    public static final String SRC_INTEST_RESOURCES = "src/intest/resources";
    public static final String INTEST_NAME = "intest";

    @Override
    public void apply(final @NotNull Project project) {
        configureIntestConfiguration(project);
        configureClasspaths(project);
        configureSources(project);
        configureIntestTask(project);
    }

    private static void configureIntestConfiguration(Project project) {
        project.getConfigurations().create("intestImplementation").extendsFrom(
                project.getConfigurations().getByName("implementation"),
                project.getConfigurations().getByName("testImplementation")
        );
        project.getConfigurations().create("intestRuntimeOnly").extendsFrom(
                project.getConfigurations().getByName("runtimeOnly")
        );
    }

    private static void configureSources(final Project project) {
        final SourceSet intestSrc = getOrCreateSourceSet(project);

        intestSrc.getResources().srcDir(SRC_INTEST_RESOURCES);

        project.getPlugins().withType(JavaPlugin.class, plugin ->
                intestSrc.getAllSource().srcDir(SRC_INTEST_JAVA));
        project.getPlugins().withType(GroovyPlugin.class, plugin ->
                intestSrc.getAllSource().srcDir(SRC_INTEST_GROOVY));
        project.getPlugins().withId("org.jetbrains.kotlin.jvm", plugin ->
                intestSrc.getAllSource().srcDir(SRC_INTEST_KOTLIN));
    }

    private static void configureClasspaths(final Project project) {
        final SourceSetContainer sourceSets =
                project.getExtensions().getByType(SourceSetContainer.class);
        final SourceSet intestSrc = getOrCreateSourceSet(project);

        intestSrc.setCompileClasspath(
                intestSrc.getCompileClasspath()
                        .plus(sourceSets.getByName("main").getOutput())
        );
        intestSrc.setRuntimeClasspath(
                intestSrc.getRuntimeClasspath()
                        .plus(sourceSets.getByName("main").getOutput())
        );
    }

    private static void configureIntestTask(final Project project) {
        final SourceSet intestSrc = getOrCreateSourceSet(project);

        project.getTasks().register(INTEST_NAME, InTest.class, task -> {
            task.setGroup("verification");
            task.setDescription("Runs integration tests.");
            task.setTestClassesDirs(intestSrc.getOutput().getClassesDirs());
            task.setClasspath(intestSrc.getRuntimeClasspath());
            task.setShouldRunAfter(Set.of("test"));
        });
    }

    private static SourceSet getOrCreateSourceSet(Project project) {
        final SourceSetContainer sourceSets =
                project.getExtensions().getByType(SourceSetContainer.class);

        if (sourceSets.getNames().contains(INTEST_NAME)) {
            return sourceSets.getByName(INTEST_NAME);
        }

        return sourceSets.create(INTEST_NAME);
    }

}
