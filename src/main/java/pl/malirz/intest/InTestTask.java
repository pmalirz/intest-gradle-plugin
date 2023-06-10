package pl.malirz.intest;

import org.gradle.api.DefaultTask;
import org.gradle.api.file.RegularFileProperty;
import org.gradle.api.tasks.OutputDirectory;
import org.gradle.api.tasks.TaskAction;

public abstract class InTestTask extends DefaultTask {

    @OutputDirectory
    abstract RegularFileProperty getGeneratedFileDir();

    public InTestTask() {
        super();
    }

    @TaskAction
    public void intest() {
        System.out.println("intest()");
    }

}
