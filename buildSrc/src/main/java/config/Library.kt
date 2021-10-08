package config

import AndroidConfig
import com.android.build.gradle.BaseExtension
import org.gradle.api.JavaVersion
import org.gradle.api.Project
import org.gradle.kotlin.dsl.getByType
import utils.getProp

private typealias AndroidBaseExtension = BaseExtension

private val buildConfigKeys = listOf(
    "BUILD_CONFIG_BI_DEMO_API_TOKEN",
    "BUILD_CONFIG_BI_DEMO_CONFIDENTIAL_CLIENT_ID",
    "BUILD_CONFIG_BI_DEMO_CONFIDENTIAL_CLIENT_SECRET",
    "BUILD_CONFIG_BI_DEMO_PUBLIC_CLIENT_ID",
    "BUILD_CONFIG_PUBLIC_API_URL",
    "BUILD_CONFIG_AUTH_URL",
    "BUILD_CONFIG_DEVICE_GATEWAY_URL",
    "BUILD_CONFIG_MIGRATED_URL",
    "BUILD_CONFIG_CHANNEL"
)

fun Project.configureAndroidLib() = this.extensions.getByType<AndroidBaseExtension>().run {

    compileSdkVersion(AndroidConfig.COMPILE_SDK_VERSION)
    buildToolsVersion(AndroidConfig.BUILD_TOOLS_VERSION)

    defaultConfig {
        minSdk = AndroidConfig.MIN_SDK_VERSION
        targetSdk = AndroidConfig.TARGET_SDK_VERSION

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")

        buildConfigKeys.forEach { key ->
            buildConfigField("String", key, "\"${getProp(key)}\"")
        }
    }

    defaultConfig {
        vectorDrawables.useSupportLibrary = true
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }

    compileOptions {
        sourceCompatibility(JavaVersion.VERSION_11)
        targetCompatibility(JavaVersion.VERSION_11)
    }

    lintOptions {
        isCheckReleaseBuilds = false
        isAbortOnError = false
    }

    testOptions {
        unitTests.isIncludeAndroidResources = true
    }
}
