import checks.ktlintCheckConfigSampleApp
import utils.getProp

plugins {
    id(libs.plugins.com.android.application.get().pluginId)
    id(libs.plugins.kotlin.android.get().pluginId)
    id(libs.plugins.org.jlleitschuh.gradle.ktlint.get().pluginId)
}

android {
    namespace = "com.beyondidentity.authenticator.sdk.android"
    compileSdk = AndroidConfig.COMPILE_SDK_VERSION
    buildToolsVersion = AndroidConfig.BUILD_TOOLS_VERSION

    defaultConfig {
        applicationId = "com.beyondidentity.authenticator.sdk.android"
        minSdk = AndroidConfig.MIN_SDK_VERSION
        targetSdk = AndroidConfig.TARGET_SDK_VERSION
        versionCode = 1
        versionName = "1.0.0" // device-info doesn't like it when it's not semantic version major.minor.patch

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        vectorDrawables {
            useSupportLibrary = true
        }

        manifestPlaceholders["acme_app_scheme"] = "acme"
        manifestPlaceholders["appAuthRedirectScheme"] = "com.okta.dev-43409302"
        manifestPlaceholders["auth0Domain"] = "dev-pt10fbkg.us.auth0.com"
        manifestPlaceholders["auth0Scheme"] = "acme"
        manifestPlaceholders["embedded_app_scheme"] = getProp("BUILD_CONFIG_BEYOND_IDENTITY_SDK_SAMPLEAPP_SCHEME")

        listOf(
            "BUILD_CONFIG_BI_DEMO_API_TOKEN",
            "BUILD_CONFIG_BI_DEMO_CONFIDENTIAL_CLIENT_ID",
            "BUILD_CONFIG_BI_DEMO_CONFIDENTIAL_CLIENT_SECRET",
            "BUILD_CONFIG_BI_DEMO_PUBLIC_CLIENT_ID",
            "BUILD_CONFIG_BEYOND_IDENTITY_SDK_SAMPLEAPP_SCHEME",
            "BUILD_CONFIG_BEYOND_IDENTITY_DEMO_TENANT",
            "BUILD_CONFIG_AUTH_URL",
            "BUILD_CONFIG_ACME_URL",
            "BUILD_CONFIG_BI_SDK_VERSION"
        ).forEach { key ->
            buildConfigField("String", key, "\"${getProp(key)}\"")
        }
    }

    buildFeatures {
        buildConfig = true
        compose = true
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
        sourceCompatibility(AndroidConfig.JAVA_VERSION)
        targetCompatibility(AndroidConfig.JAVA_VERSION)
    }

    composeOptions {
        kotlinCompilerExtensionVersion = libs.versions.androidx.compose.compiler.get()
    }

    kotlinOptions {
        jvmTarget = AndroidConfig.JAVA_VERSION.toString()
        freeCompilerArgs += listOf(
            "-Xopt-in=kotlin.RequiresOptIn",
        )
    }

    lint {
        checkReleaseBuilds = false
        abortOnError = false
    }

    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
            excludes += "/META-INF/okta/version.properties"
        }
    }

    flavorDimensions += "default"
    productFlavors {
        create("devel") {
            applicationIdSuffix = ".devel"
            versionNameSuffix = "-devel"
        }
        create("rolling") {
            applicationIdSuffix = ".rolling"
            versionNameSuffix = "-rolling"
        }
        create("staging") {
            applicationIdSuffix = ".staging"
            versionNameSuffix = "-staging"
        }
        create("production") {}
    }

    sourceSets {
        getByName("androidTest") {
            java.srcDir("src/androidTest/kotlin")
        }
        getByName("main") {
            java.srcDir("src/main/kotlin")
        }
        getByName("test") {
            java.srcDir("src/test/kotlin")
        }
    }

    testOptions {
        animationsDisabled = true
    }
}

ktlintCheckConfigSampleApp()

dependencies {
    // Android Test Dependencies
    androidTestImplementation(libs.androidx.compose.ui.test.junit4)
    androidTestImplementation(libs.androidx.test.espresso.core)
    androidTestImplementation(libs.junit)
    debugImplementation(libs.androidx.compose.ui.test.manifest)
    debugImplementation(libs.androidx.compose.ui.tooling)

    implementation(project(Modules.EMBEDDED))
//    implementation(project(Modules.EMBEDDED_UI))
    implementation(libs.kotlin.stdlib.jdk8)
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.compose.material)
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.common)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.lifecycle.viewmodel.compose)
    implementation(libs.androidx.lifecycle.viewmodel.ktx)
    implementation(libs.androidx.lifecycle.viewmodel.savedstate)
    implementation(libs.material)

    implementation(libs.jwtdecode)
    implementation(libs.timber)

    // Auth0
    implementation(libs.auth0)

    // Coroutines
    implementation(libs.kotlinx.coroutines.android)

    // Okta
    implementation(libs.okta.idx.java.api)
    implementation(libs.okta.jwt.verifier)
    implementation(libs.okta.jwt.verifier.impl)
    implementation(libs.okta.oidc.android)

    // Square
    implementation(libs.gson)
    implementation(libs.okhttp)
    implementation(libs.okhttp.logging.interceptor)
    implementation(libs.retrofit)
    implementation(libs.retrofit.converter.gson)
}
