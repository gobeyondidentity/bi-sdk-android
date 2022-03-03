// Top-level build file where you can add configuration options common to all sub-projects/modules.

import utils.getProp

buildscript {
    repositories {
        google()
        mavenCentral()
        maven("https://plugins.gradle.org/m2/")
    }
    dependencies {
        classpath(Classpaths.ANDROID_GRADLE_TOOLS_PLUGIN)
        classpath(Classpaths.KOTLIN_GRADLE_PLUGIN)
        classpath(Classpaths.KOTLINX_SERIALIZATION_PLUGIN)
        classpath(Classpaths.KTLINT_PLUGIN)
        classpath(Classpaths.DOKKA_PLUGIN)
        // NOTE: Do not place your application dependencies here; they belong
        // in the individual module build.gradle files
    }
}

allprojects {
    repositories {
        mavenLocal()
        mavenCentral()
        google()
        maven("https://jitpack.io")
        maven(getProp("BUILD_CONFIG_CLOUDSMITH_REPO_URL"))
    }
}

task<Delete>("clean") {
    delete(rootProject.buildDir)
}
