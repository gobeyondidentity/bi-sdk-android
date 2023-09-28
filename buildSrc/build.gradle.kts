plugins {
    `kotlin-dsl`
    id("maven-publish")
    id("org.jlleitschuh.gradle.ktlint") version "11.1.0"
}

repositories {
    google()
    maven("https://plugins.gradle.org/m2/")
    mavenCentral()
}

dependencies {
    implementation("com.android.tools.build:gradle:7.4.2")
    implementation("org.jetbrains.kotlin:kotlin-gradle-plugin:1.8.0")
    implementation("org.jlleitschuh.gradle:ktlint-gradle:11.1.0")
}
