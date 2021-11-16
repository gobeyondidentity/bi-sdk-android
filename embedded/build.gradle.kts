import checks.ktlintCheckConfig
import config.configureAndroidLib
import config.configureMavenPublish
import utils.getProp

plugins {
    id("com.android.library")
    id("kotlin-android")
    id("kotlinx-serialization")
    id("maven-publish")
    id("org.jlleitschuh.gradle.ktlint")
    id("kotlin-parcelize")
    id("org.jetbrains.dokka")
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
        jvmTarget = "11"
    }
}

ktlintCheckConfig()

configureMavenPublish(
    groupIdForLib = "com.beyondidentity.android.sdk",
    artifactIdForLib = "embedded"
)

val biSdkVersion = getProp("BUILD_CONFIG_BI_SDK_VERSION")

dependencies {
    implementation(Libs.KOTLIN_STD_LIB)
    implementation(Libs.KOTLIN_REFLECTION)
    implementation(Libs.KOTLINX_SERIALIZATION)
    implementation(Libs.KOTLINX_COROUTINES_ANDROID)
    implementation(Libs.ANDROIDX_PREFERENCES)
    implementation(Libs.ANDROIDX_BIOMETRICS)
    implementation(Libs.ZXING_EMBEDDED)
    implementation("${Libs.CORE}:$biSdkVersion")
    implementation("${Libs.ENCLAVE}:$biSdkVersion")
    implementation("${Libs.DEVICE_INFO}:$biSdkVersion")
    implementation("${Libs.LOG}:$biSdkVersion")
    implementation("${Libs.PROTO}:$biSdkVersion")
    implementation("${Libs.PRELUDE}:$biSdkVersion")
    implementation("${Libs.OPTICS}:$biSdkVersion")

    testImplementation(TestLibs.JUNIT)
    testImplementation(TestLibs.ANDROIDX_TEST_CORE)
    testImplementation(TestLibs.ROBOLECTRIC)

    androidTestImplementation(TestLibs.ANDROIDX_TEST_CORE)
    androidTestImplementation(AndroidTestLibs.ANDROIDX_ESPRESSO_CORE)
    androidTestImplementation(AndroidTestLibs.ANDROIDX_EXT_JUNIT)
    androidTestImplementation(AndroidTestLibs.ANDROIDX_RUNNER)
    androidTestImplementation(AndroidTestLibs.ANDROIDX_RULES)
    androidTestImplementation(AndroidTestLibs.MOCKITO_CORE)
    androidTestImplementation(AndroidTestLibs.MOCKITO_ANDROID)
}
