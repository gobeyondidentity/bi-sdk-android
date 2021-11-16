import checks.ktlintCheckConfig
import config.configureAndroidLib
import config.configureMavenPublish

plugins {
    id("com.android.library")
    id("kotlin-android")
    id("maven-publish")
    id("org.jlleitschuh.gradle.ktlint")
    id("org.jetbrains.dokka")
}

configureAndroidLib()

// TODO: Move into a function like `configureMavenPublish`
tasks.withType<org.jetbrains.dokka.gradle.DokkaTask>().configureEach {
    dokkaSourceSets {
        named("main") {
            moduleName.set("Authenticator SDK")
        }
    }
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
    kotlinOptions {
        jvmTarget = "11"
    }
}

ktlintCheckConfig()

configureMavenPublish(
    groupIdForLib = "com.beyondidentity.android.sdk",
    artifactIdForLib = "authenticator"
)

dependencies {
    implementation(Libs.KOTLIN_STD_LIB)
    implementation(Libs.KOTLIN_REFLECTION)
    implementation(Libs.ANDROIDX_BROWSER)
    implementation(Libs.ANDROIDX_CONSTRAINT_LAYOUT)

    testImplementation(TestLibs.JUNIT)

    androidTestImplementation(AndroidTestLibs.ANDROIDX_EXT_JUNIT)
    androidTestImplementation(AndroidTestLibs.ANDROIDX_ESPRESSO_CORE)
}
