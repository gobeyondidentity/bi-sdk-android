@file:Suppress("SuspiciousCollectionReassignment")

import checks.ktlintCheckConfigSampleApp
import utils.getProp

plugins {
    id("com.android.application")
    id("kotlin-android")
    id("org.jlleitschuh.gradle.ktlint")
}

android {
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

    kotlinOptions {
        jvmTarget = AndroidConfig.JAVA_VERSION.toString()
        freeCompilerArgs += listOf(
            "-Xopt-in=kotlin.RequiresOptIn",
        )
    }

    buildFeatures {
        compose = true
    }

    composeOptions {
        kotlinCompilerVersion = Versions.KOTLIN
        kotlinCompilerExtensionVersion = Versions.ANDROIDX_COMPOSE
    }

    packagingOptions {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
            excludes += "/META-INF/okta/version.properties"
        }
    }

    lint {
        checkReleaseBuilds = false
        abortOnError = false
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
}

ktlintCheckConfigSampleApp()

dependencies {
    debugImplementation(DebugLibs.ANDROIDX_COMPOSE_UI_TOOLING)

    implementation(project(Modules.EMBEDDED))
//    implementation(project(Modules.EMBEDDED_UI))
    implementation(Libs.KOTLIN_STD_LIB)
    implementation(Libs.ANDROIDX_ACTIVITY_COMPOSE)
    implementation(Libs.ANDROIDX_APPCOMPAT)
    implementation(Libs.ANDROIDX_COMPOSE_MATERIAL)
    implementation(Libs.ANDROIDX_COMPOSE_UI)
    implementation(Libs.ANDROIDX_COMPOSE_UI_TOOLING_PREVIEW)
    implementation(Libs.ANDROIDX_CONSTRAINT_LAYOUT)
    implementation(Libs.ANDROIDX_CORE_KTX)
    implementation(Libs.ANDROIDX_LIFECYCLE_COMMON)
    implementation(Libs.ANDROIDX_LIFECYCLE_RUNTIME_KTX)
    implementation(Libs.ANDROIDX_LIFECYCLE_VIEWMODEL_COMPOSE)
    implementation(Libs.ANDROIDX_LIFECYCLE_VIEWMODEL_KTX)
    implementation(Libs.ANDROIDX_LIFECYCLE_VIEWMODEL_SAVEDSTATE)
    implementation(Libs.MATERIAL)

    implementation(Libs.JWTDECODE)
    implementation(Libs.TIMBER)

    // Auth0
    implementation(Libs.AUTH0)

    // Coroutines
    implementation(Libs.KOTLINX_COROUTINES_ANDROID)

    // Okta
    implementation(Libs.OKTA_IDX_JAVA_API)
    implementation(Libs.OKTA_JWT_VERIFIER)
    implementation(Libs.OKTA_JWT_VERIFIER_IMPL)
    implementation(Libs.OKTA_OIDC_ANDROID)

    // Square
    implementation(Libs.GSON)
    implementation(Libs.OKHTTP)
    implementation(Libs.OKHTTP_LOGGING_INTERCEPTOR)
    implementation(Libs.RETROFIT)
    implementation(Libs.RETROFIT_CONVERTER_GSON)
}
