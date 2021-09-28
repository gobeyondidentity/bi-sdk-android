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
    implementation("com.android.tools.build:gradle:7.0.2")
    implementation("org.jetbrains.kotlin:kotlin-gradle-plugin:1.4.31")
    implementation("org.jlleitschuh.gradle:ktlint-gradle:10.0.0")
}
