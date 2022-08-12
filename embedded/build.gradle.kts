@file:Suppress("SuspiciousCollectionReassignment")

import checks.ktlintCheckConfig
import config.configureAndroidLib
import config.configureMavenPublish
import utils.getProp

plugins {
    id("com.android.library")
    id("kotlin-android")
    id("kotlin-parcelize")
    id("kotlinx-serialization")
    id("maven-publish")
    id("org.jetbrains.dokka")
    id("org.jlleitschuh.gradle.ktlint")
}

configureAndroidLib()

// TODO: Move into a function like `configureMavenPublish`
tasks.withType<org.jetbrains.dokka.gradle.DokkaTask>().configureEach {
    dokkaSourceSets {
        named("main") {
            moduleName.set("Embedded SDK")
        }
    }
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
    kotlinOptions {
        jvmTarget = AndroidConfig.JAVA_VERSION.toString()
        freeCompilerArgs += listOf(
            "-Xopt-in=kotlin.RequiresOptIn",
        )
    }
}

ktlintCheckConfig()

configureMavenPublish(
    groupIdForLib = "com.beyondidentity.android.sdk",
    artifactIdForLib = "embedded"
)

val biSdkVersion = getProp("BUILD_CONFIG_BI_SDK_VERSION")

dependencies {
    implementation(Libs.ANDROIDX_BIOMETRIC)
    implementation(Libs.ANDROIDX_PREFERENCES_KTX)
    implementation(Libs.KOTLINX_COROUTINES_ANDROID)
    implementation(Libs.KOTLINX_SERIALIZATION_JSON)
    implementation(Libs.KOTLIN_REFLECTION)
    implementation(Libs.KOTLIN_STD_LIB)
    implementation(Libs.TIMBER)
    implementation(Libs.ZXING_EMBEDDED)
    implementation("${Libs.CORE}:$biSdkVersion")
    implementation("${Libs.DEVICE_INFO}:$biSdkVersion")
    implementation("${Libs.ENCLAVE}:$biSdkVersion")
    implementation("${Libs.LOG}:$biSdkVersion")
    implementation("${Libs.OPTICS}:$biSdkVersion")
    implementation("${Libs.PRELUDE}:$biSdkVersion")
    implementation("${Libs.PROTO}:$biSdkVersion")

    testImplementation(TestLibs.ANDROIDX_TEST_CORE)
    testImplementation(TestLibs.JUNIT)
    testImplementation(TestLibs.KOTLINX_COROUTINES_TEST)
    testImplementation(TestLibs.ROBOLECTRIC)

    androidTestImplementation(TestLibs.ANDROIDX_TEST_CORE)
    androidTestImplementation(TestLibs.KOTLINX_COROUTINES_TEST)
    androidTestImplementation(AndroidTestLibs.ANDROIDX_ESPRESSO_CORE)
    androidTestImplementation(AndroidTestLibs.ANDROIDX_EXT_JUNIT)
    androidTestImplementation(AndroidTestLibs.ANDROIDX_RULES)
    androidTestImplementation(AndroidTestLibs.ANDROIDX_RUNNER)
    androidTestImplementation(AndroidTestLibs.MOCKITO_ANDROID)
    androidTestImplementation(AndroidTestLibs.MOCKITO_CORE)
}
