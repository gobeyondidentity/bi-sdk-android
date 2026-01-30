// Top-level build file where you can add configuration options common to all sub-projects/modules.
import utils.getProp

buildscript {
    repositories {
        google()
        mavenCentral()
        mavenLocal()
        maven("https://jitpack.io")
        maven("https://plugins.gradle.org/m2/")
    }

    dependencies {
        classpath(libs.android.gradle.plugin)
        classpath(libs.android.documentation.plugin)
        classpath(libs.dokka.gradle.plugin)
        classpath(libs.kotlin.gradle.plugin)
        classpath(libs.kotlin.serialization.plugin)
        classpath(libs.ktlint.gradle.plugin)
        // NOTE: Do not place your application dependencies here; they belong
        // in the individual module build.gradle files
    }
}

plugins {
    alias(libs.plugins.org.jetbrains.kotlin.plugin.compose) apply false
}

allprojects {
    repositories {
        google()
        mavenCentral()
        mavenLocal()
        maven("https://jitpack.io")
        maven("https://plugins.gradle.org/m2/")
        val cloudsmithUrl = getProp("BUILD_CONFIG_CLOUDSMITH_REPO_URL")
        if (cloudsmithUrl != "") {
            maven(cloudsmithUrl)
        }
    }
}

tasks.register<Delete>("clean") {
    delete(rootProject.layout.buildDirectory)
}
