# intest-gradle-plugin
[![Build](https://github.com/pmalirz/intest-gradle-plugin/actions/workflows/build.yaml/badge.svg)](https://github.com/pmalirz/intest-gradle-plugin/actions/workflows/build.yaml)
[![Gradle Plugin Portal](https://img.shields.io/badge/Version-1.0.1-green.svg)](https://plugins.gradle.org/plugin/pl.malirz.intest)


<img src="./docs/images/intest-logo.png" title="VShop Logo" width="150" height="150"/>

## Overview

`gradlew intest`

**intest-gradle-plugin** is a Gradle plugin that simplifies the configuration of integration tests. The plugin adds
the `intest` task to a Gradle project's configuration, which helps to avoid manual configuration of integration tests.
This follows the convention over configuration principle.

Once the **intest-gradle-plugin** is enabled in the project the IDE (e.g. IntelliJ) 
automatically discovers the `intest` folder as a source folder. 
You can start adding integration tests as per your preference, Java, Kotlin or Groovy (Spock).

## Usage

The `intest` task can be executed using the following command:

```bash
gradlew intest
```

Although the `intest` task should run after the `test` task, it does not run automatically by default. You may need to
execute it explicitly or configure the `intest` task to be run automatically immediately after the `test` phase.

The reason why the `intest` task does not run automatically by default is that integration tests are usually much
heavier to run. Therefore, it is wise to make a conscious decision on when to execute them.

Latest version of the plugin is available on [Gradle Plugin Portal](https://plugins.gradle.org/plugin/pl.malirz.intest).

## Adding the Plugin to Your Project

`settings.gradle.kts`:

```kotlin
pluginManagement {
    plugins {
        id("pl.malirz.intest") version "1.0.1"
    }
    repositories {
        gradlePluginPortal()
    }
}
```

`build.gradle.kts`:

```kotlin
plugins {
    id("pl.malirz.intest")
}

tasks.withType<InTest> {
    useJUnitPlatform()
}
```

Folders structure in your project

```
project
│   build.gradle.kts
│   settings.gradle.kts
└───src
    └───main/
    └───test/
    └───intest/
```

---

Happy using! 👋

👍 Like it\
🌠 Star it\
📥 Take it
