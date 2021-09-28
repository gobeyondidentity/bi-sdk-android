import kotlin.collections.listOf
import utils.getProp

plugins {
    id("com.android.application")
    id("kotlin-android")
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

        manifestPlaceholders["embedded_app_scheme"] = getProp("BUILD_CONFIG_BEYOND_IDENTITY_SDK_SAMPLEAPP_SCHEME")

        listOf(
                "BUILD_CONFIG_BI_DEMO_API_TOKEN",
                "BUILD_CONFIG_BI_DEMO_CONFIDENTIAL_CLIENT_ID",
                "BUILD_CONFIG_BI_DEMO_CONFIDENTIAL_CLIENT_SECRET",
                "BUILD_CONFIG_BI_DEMO_PUBLIC_CLIENT_ID",
                "BUILD_CONFIG_BEYOND_IDENTITY_SDK_SAMPLEAPP_SCHEME",
                "BUILD_CONFIG_BEYOND_IDENTITY_DEMO_TENANT",
                "BUILD_CONFIG_AUTH_URL",
                "BUILD_CONFIG_ACME_URL"
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

    lint {
        isCheckReleaseBuilds = false
        isAbortOnError = false
    }
}

dependencies {
    implementation(project(":authenticator"))
    implementation(project(":embedded"))
    implementation(project(":embedded-ui"))
    implementation("org.jetbrains.kotlin:kotlin-stdlib:${Versions.KOTLIN}")
    implementation("androidx.core:core-ktx:1.6.0")
    implementation("androidx.appcompat:appcompat:1.3.1")
    implementation("com.google.android.material:material:1.4.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.0")
    implementation("com.jakewharton.timber:timber:4.7.1")

    //LifeCycle
    implementation("androidx.lifecycle:lifecycle-common:2.3.1")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.3.1")
    //Retrofit
    implementation("com.squareup.okhttp3:okhttp:4.9.0")
    implementation("com.squareup.retrofit2:retrofit:2.7.1")
    implementation("com.google.code.gson:gson:2.8.6")
    implementation("com.squareup.retrofit2:converter-gson:2.5.0")
    implementation("com.squareup.okhttp3:logging-interceptor:4.9.0")

    //Coroutines
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.4.2")

    implementation("com.auth0.android:jwtdecode:2.0.0")
}
