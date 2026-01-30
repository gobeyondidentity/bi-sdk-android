plugins {
    `kotlin-dsl`
    id("maven-publish")
    id("org.jlleitschuh.gradle.ktlint") version "13.1.0"
}

repositories {
    google()
    maven("https://plugins.gradle.org/m2/")
    mavenCentral()
}

dependencies {
    implementation("com.android.tools.build:gradle:8.13.2")
    implementation("org.jetbrains.kotlin:kotlin-gradle-plugin:2.2.0")
    implementation("org.jlleitschuh.gradle:ktlint-gradle:13.1.0")

    // Temporary
    implementation("io.netty:netty-handler:4.1.122.Final")  // Remove with Android Gradle Plugin ???
    implementation("org.jdom:jdom2:2.0.6.1")                // Remove with Android Gradle Plugin ???
}

ktlint {
    version.set("1.7.1")
}
