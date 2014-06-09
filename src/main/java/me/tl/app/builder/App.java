package me.tl.app.builder;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.DefaultLogger;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.ProjectHelper;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import java.io.File;

public class App implements Runnable {


    public App() {

    }

    public static void main(String[] args) {
        new App().run();
    }

    @Override
    public void run() {
        final Resource res = new ClassPathResource("build.xml");
        if (res.exists()) {
            try {
                final File buildFile = res.getFile();
                final Project p = new Project();

                final DefaultLogger consoleLogger = new DefaultLogger();
                consoleLogger.setErrorPrintStream(System.err);
                consoleLogger.setOutputPrintStream(System.out);
                consoleLogger.setMessageOutputLevel(Project.MSG_INFO);
                p.addBuildListener(consoleLogger);

                try {

                    p.setUserProperty("ant.file", buildFile.getAbsolutePath());
                    p.fireBuildStarted();
                    p.init();
                    ProjectHelper helper = ProjectHelper.getProjectHelper();
                    p.addReference("ant.projectHelper", helper);
                    helper.parse(p, buildFile);

                    p.executeTarget(p.getDefaultTarget());
                    p.fireBuildFinished(null);
                } catch (BuildException e) {
                    p.fireBuildFinished(e);
                }
            } catch (Exception exception) {
                throw new RuntimeException(exception);
            }

        }
    }
}