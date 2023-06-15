plugins {
    `java-gradle-plugin`
    `maven-publish`
    groovy
    id("com.gradle.plugin-publish")
}

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
    website.set("https://github.com/pmalirz/intest-gradle-plugin")
    vcsUrl.set("https://github.com/pmalirz/intest-gradle-plugin.git")
    plugins {
        create("intestPlugin") {
            id = "pl.malirz.intest"
            displayName = "InTest Gradle Plugin"
            description = "Enables integration test in a project"
            @Suppress("UnstableApiUsage")
            tags.set(listOf("testing", "integrationTesting"))
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