plugins {
    `kotlin-dsl`
    id("maven-publish")
    id("org.jlleitschuh.gradle.ktlint") version "10.0.0"
}

repositories {
    google()
    maven("https://plugins.gradle.org/m2/")
    mavenCentral()
}

dependencies {
    implementation("com.android.tools.build:gradle:7.1.1")
    implementation("org.jetbrains.kotlin:kotlin-gradle-plugin:1.6.10")
    implementation("org.jlleitschuh.gradle:ktlint-gradle:10.0.0")
}
