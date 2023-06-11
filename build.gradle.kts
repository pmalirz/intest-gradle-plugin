plugins {
    `java-gradle-plugin`
    `maven-publish`
}

group = "pl.malirz"
version = "0.0.1-SNAPSHOT"

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(11))
    }
}

repositories {
    mavenCentral()
}

dependencies {
    implementation(gradleApi())

    // groovy 4 and spock 2
    testImplementation("org.spockframework:spock-core:2.3-groovy-3.0")
}

tasks.withType<Test> {
    useJUnitPlatform()
}

gradlePlugin {
    plugins {
        create("intestPlugin") {
            id = "pl.malirz.intest"
            displayName = "Integration Test Plugin"
            description = "Adds integration test task to project"
            tags.set(listOf("source", "integration-test", "test"))
            implementationClass = "pl.malirz.intest.InTestPlugin"
        }
    }
}

// publish to maven repository
publishing {
    publications {
        create<MavenPublication>("mavenJava") {
            from(components["java"])
        }
    }
    repositories {
        maven {
            name = "GitHubPackages"
            url = uri("https://maven.pkg.github.com/pmalirz/intest-gradle-plugin")
            credentials {
                username = project.findProperty("gpr.user") as String?
                password = project.findProperty("gpr.key") as String?
            }
        }
    }
}