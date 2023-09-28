import checks.ktlintCheckConfig
import config.configureAndroidLib
import config.configureMavenPublish
import utils.getProp

plugins {
    id(libs.plugins.com.android.library.get().pluginId)
    id(libs.plugins.kotlin.android.get().pluginId)
    id(libs.plugins.kotlin.parcelize.get().pluginId)
    id(libs.plugins.kotlinx.serialization.get().pluginId)
    id(libs.plugins.maven.publish.get().pluginId)
    id(libs.plugins.org.jetbrains.dokka.get().pluginId)
    id(libs.plugins.org.jlleitschuh.gradle.ktlint.get().pluginId)
}

android {
    namespace = "com.beyondidentity.authenticator.sdk.embedded"
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
    implementation(libs.androidx.biometric)
    implementation(libs.androidx.browser)
    implementation(libs.androidx.preference.ktx)
    implementation(libs.kotlinx.coroutines.android)
    implementation(libs.kotlinx.serialization.json)
    implementation(libs.kotlin.reflect)
    implementation(libs.kotlin.stdlib.jdk8)
    implementation(libs.timber)
    implementation(libs.zxing.android.embedded)
    implementation("${libs.core.get()}:$biSdkVersion")
    implementation("${libs.device.info.get()}:$biSdkVersion")
    implementation("${libs.enclave.get()}:$biSdkVersion")
    implementation("${libs.log.get()}:$biSdkVersion")
    implementation("${libs.optics.get()}:$biSdkVersion")
    implementation("${libs.prelude.get()}:$biSdkVersion")
    implementation("${libs.proto.get()}:$biSdkVersion")

    testImplementation(libs.androidx.test.core)
    testImplementation(libs.junit)
    testImplementation(libs.kotlinx.coroutines.test)
    testImplementation(libs.robolectric)

    androidTestImplementation(libs.androidx.test.core)
    androidTestImplementation(libs.androidx.test.espresso.core)
    androidTestImplementation(libs.androidx.test.ext.junit)
    androidTestImplementation(libs.androidx.test.rules)
    androidTestImplementation(libs.androidx.test.runner)
    androidTestImplementation(libs.kotlinx.coroutines.test)
    androidTestImplementation(libs.mockito.android)
    androidTestImplementation(libs.mockito.core)
}
