plugins {
    `kotlin-dsl`
    id("maven-publish")
    id("org.jlleitschuh.gradle.ktlint") version "12.3.0"
}

repositories {
    google()
    maven("https://plugins.gradle.org/m2/")
    mavenCentral()
}

dependencies {
    implementation("com.android.tools.build:gradle:8.10.1")
    implementation("org.jetbrains.kotlin:kotlin-gradle-plugin:2.1.20")
    implementation("org.jlleitschuh.gradle:ktlint-gradle:12.3.0")

    // Temporary
    implementation("io.netty:netty-handler:4.1.122.Final")  // Remove with Android Gradle Plugin ???
    implementation("org.jdom:jdom2:2.0.6.1")                // Remove with Android Gradle Plugin ???
}

ktlint {
    version.set("1.6.0")
}
