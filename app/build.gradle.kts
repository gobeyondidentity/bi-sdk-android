import kotlin.collections.listOf
import utils.getProp

plugins {
    id("com.android.application")
    id("kotlin-android")
}

val composeVersion = "1.2.0-alpha02"

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
        sourceCompatibility(JavaVersion.VERSION_11)
        targetCompatibility(JavaVersion.VERSION_11)
    }

    kotlinOptions {
        jvmTarget = "11"
    }

    buildFeatures {
        compose = true
    }

    composeOptions {
        kotlinCompilerExtensionVersion = composeVersion
    }

    packagingOptions {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
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

dependencies {
    debugImplementation("androidx.compose.ui:ui-tooling:$composeVersion")

    implementation(project(":authenticator"))
    implementation(project(":embedded"))
    implementation(project(":embedded-ui"))
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8:${Versions.KOTLIN}")
    implementation("androidx.core:core-ktx:1.7.0")
    implementation("androidx.appcompat:appcompat:1.4.1")
    implementation("com.google.android.material:material:1.5.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.3")
    implementation("androidx.compose.ui:ui:$composeVersion")
    implementation("androidx.compose.material:material:$composeVersion")
    implementation("androidx.compose.ui:ui-tooling-preview:$composeVersion")
    implementation("androidx.activity:activity-compose:1.5.0-alpha02")
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.4.1")
    implementation("androidx.lifecycle:lifecycle-viewmodel-savedstate:2.4.1")
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.4.1")

    implementation("com.jakewharton.timber:timber:4.7.1")

    //LifeCycle
    implementation("androidx.lifecycle:lifecycle-common:2.4.1")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.4.1")
    //Retrofit
    implementation("com.squareup.okhttp3:okhttp:4.9.0")
    implementation("com.squareup.retrofit2:retrofit:2.7.1")
    implementation("com.google.code.gson:gson:2.8.6")
    implementation("com.squareup.retrofit2:converter-gson:2.5.0")
    implementation("com.squareup.okhttp3:logging-interceptor:4.9.0")

    //Coroutines
    implementation(Libs.KOTLINX_COROUTINES_ANDROID)

    implementation("com.auth0.android:jwtdecode:2.0.0")
}
