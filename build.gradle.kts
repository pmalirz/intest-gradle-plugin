plugins {
    `java-gradle-plugin`
    `maven-publish`
    `groovy`
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

    testImplementation("org.spockframework:spock-core:2.3-groovy-3.0")
    testImplementation(gradleTestKit())
    testImplementation("commons-io:commons-io:2.11.0")

    testImplementation("org.junit.jupiter:junit-jupiter-api:5.9.2")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.9.2")
    testImplementation("org.junit.jupiter:junit-jupiter-params:5.9.2")
}

tasks.withType<Test> {
    useJUnitPlatform()
}

gradlePlugin {
    plugins {
        create("intestPlugin") {
            id = "pl.malirz.intest"
            displayName = "Integration Tests"
            description = "Adds integration test task to project"
            @Suppress("UnstableApiUsage")
            tags.set(listOf("source", "integration-test", "test"))
            implementationClass = "pl.malirz.intest.InTestPlugin"
        }
    }
}

// publish to maven repository
publishing {
    publications {
        create<MavenPublication>("gpr") {
            from(components["java"])
        }
    }
    repositories {
        maven {
            name = "GitHubPackages"
            url = uri("https://maven.pkg.github.com/pmalirz/intest-gradle-plugin")
            credentials {
                username = project.findProperty("gpr.user") as String? ?: System.getenv("USERNAME")
                password = project.findProperty("gpr.key") as String? ?: System.getenv("TOKEN")
            }
        }
    }
}