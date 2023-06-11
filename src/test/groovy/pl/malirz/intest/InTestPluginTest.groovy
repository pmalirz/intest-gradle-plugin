package pl.malirz.intest


import spock.lang.Specification
import spock.lang.TempDir

class InTestPluginTest extends Specification {

    @TempDir
    File testProjectDir
    private File settingsFile
    private File buildFile

    def setup() {
        settingsFile = new File(testProjectDir, "settings.gradle")
        buildFile = new File(testProjectDir, "build.gradle")
    }

    def "should run test from the `intest` source set"() {
        when:
        println("testProjectDir: " + testProjectDir)

        then:
        noExceptionThrown()

    }
}
