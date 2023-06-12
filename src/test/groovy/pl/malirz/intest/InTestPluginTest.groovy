package pl.malirz.intest

import org.apache.commons.io.FileUtils
import org.gradle.testkit.runner.GradleRunner
import spock.lang.Specification
import spock.lang.TempDir

class InTestPluginTest extends Specification {

    @TempDir
    File testProjectDir

    private final String expectedIntestReport = "build/test-results/intest/TEST-DummyTest.xml"

    def setup() {
        println("Test project temporary location: " + testProjectDir)
        FileUtils.copyDirectory(new File("src/test/resources/testproject"), testProjectDir)
    }

    def "should run test from the `intest` source set successfully"() {
        when:
        def gradleRunner = GradleRunner.create()
                .withProjectDir(testProjectDir)
                .withPluginClasspath()
                .withArguments("intest")
                .build()

        then:
        noExceptionThrown()
        gradleRunner.output.contains("BUILD SUCCESSFUL")
        gradleRunner.output.contains("DummyTest.test SUCCESS")
        FileUtils.getFile(testProjectDir, expectedIntestReport).exists()
    }
}
