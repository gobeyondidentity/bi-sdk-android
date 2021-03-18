plugins {
    `kotlin-dsl`
    id("maven-publish")
}

repositories {
    jcenter()
    google()
}

dependencies {
    implementation("com.android.tools.build:gradle:4.1.2")
    implementation("org.jetbrains.kotlin:kotlin-gradle-plugin:1.4.31")
}