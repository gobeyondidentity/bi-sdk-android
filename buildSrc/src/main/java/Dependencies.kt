object Versions {
    const val KOTLIN = "1.6.10"
    const val KOTLINX_COROUTINES = "1.6.2"
    const val KOTLINX_SERIALIZATION = "1.0.1"
    const val ANDROIDX_ACTIVITY = "1.5.0-alpha02"
    const val ANDROIDX_APPCOMPAT = "1.4.1"
    const val ANDROIDX_BIOMETRIC = "1.1.0"
    const val ANDROIDX_BROWSER = "1.4.0"
    const val ANDROIDX_COMPOSE = "1.2.0-alpha02"
    const val ANDROIDX_CONSTRAINT_LAYOUT = "2.1.3"
    const val ANDROIDX_CORE = "1.7.0"
    const val ANDROIDX_LIFECYCLE = "2.4.1"
    const val ANDROIDX_PREFERENCES = "1.1.1"
    const val AUTH0 = "2.8.0"
    const val CODE_SCANNER = "2.1.0"
    const val GSON = "2.8.6"
    const val JWTDECODE = "2.0.0"
    const val MATERIAL = "1.5.0"
    const val OKHTTP = "4.9.0"
    const val OKTA_IDX_JAVA_API = "3.0.2"
    const val OKTA_JWT_VERIFIER = "0.5.3"
    const val OKTA_OIDC_ANDROID = "1.3.2"
    const val RETROFIT = "2.7.1"
    const val TIMBER = "4.7.1"
    const val ZXING_EMBEDDED = "4.1.0"

    // Debug

    // Test
    const val ANDROIDX_TEST = "1.4.0"
    const val JUNIT = "4.13.2"
    const val ROBOLECTRIC = "4.6.1"

    // Android Test
    const val ANDROIDX_ESPRESSO = "3.4.0"
    const val ANDROIDX_EXT_JUNIT = "1.1.3"
    const val MOCKITO = "3.6.0"

    // Classpath
    const val ANDROID_GRADLE_PLUGIN = "7.1.1"
    const val DOKKA = "1.5.0"
    const val KTLINT = "10.3.0"
}

object Libs {
    // Kotlin
    const val KOTLIN_REFLECTION = "org.jetbrains.kotlin:kotlin-reflect:${Versions.KOTLIN}"
    const val KOTLIN_STD_LIB = "org.jetbrains.kotlin:kotlin-stdlib-jdk8:${Versions.KOTLIN}"
    const val KOTLINX_COROUTINES_ANDROID =
        "org.jetbrains.kotlinx:kotlinx-coroutines-android:${Versions.KOTLINX_COROUTINES}"
    const val KOTLINX_SERIALIZATION_JSON =
        "org.jetbrains.kotlinx:kotlinx-serialization-json:${Versions.KOTLINX_SERIALIZATION}"

    // Androidx
    const val ANDROIDX_ACTIVITY_COMPOSE =
        "androidx.activity:activity-compose:${Versions.ANDROIDX_ACTIVITY}"
    const val ANDROIDX_APPCOMPAT = "androidx.appcompat:appcompat:${Versions.ANDROIDX_APPCOMPAT}"
    const val ANDROIDX_BIOMETRIC = "androidx.biometric:biometric:${Versions.ANDROIDX_BIOMETRIC}"
    const val ANDROIDX_BROWSER = "androidx.browser:browser:${Versions.ANDROIDX_BROWSER}"
    const val ANDROIDX_COMPOSE_MATERIAL =
        "androidx.compose.material:material:${Versions.ANDROIDX_COMPOSE}"
    const val ANDROIDX_COMPOSE_UI = "androidx.compose.ui:ui:${Versions.ANDROIDX_COMPOSE}"
    const val ANDROIDX_COMPOSE_UI_TOOLING_PREVIEW =
        "androidx.compose.ui:ui-tooling-preview:${Versions.ANDROIDX_COMPOSE}"
    const val ANDROIDX_CONSTRAINT_LAYOUT =
        "androidx.constraintlayout:constraintlayout:${Versions.ANDROIDX_CONSTRAINT_LAYOUT}"
    const val ANDROIDX_CORE_KTX = "androidx.core:core-ktx:${Versions.ANDROIDX_CORE}"
    const val ANDROIDX_LIFECYCLE_COMMON =
        "androidx.lifecycle:lifecycle-common:${Versions.ANDROIDX_LIFECYCLE}"
    const val ANDROIDX_LIFECYCLE_RUNTIME_KTX =
        "androidx.lifecycle:lifecycle-runtime-ktx:${Versions.ANDROIDX_LIFECYCLE}"
    const val ANDROIDX_LIFECYCLE_VIEWMODEL_COMPOSE =
        "androidx.lifecycle:lifecycle-viewmodel-compose:${Versions.ANDROIDX_LIFECYCLE}"
    const val ANDROIDX_LIFECYCLE_VIEWMODEL_KTX =
        "androidx.lifecycle:lifecycle-viewmodel-ktx:${Versions.ANDROIDX_LIFECYCLE}"
    const val ANDROIDX_LIFECYCLE_VIEWMODEL_SAVEDSTATE =
        "androidx.lifecycle:lifecycle-viewmodel-savedstate:${Versions.ANDROIDX_LIFECYCLE}"
    const val ANDROIDX_PREFERENCES_KTX =
        "androidx.preference:preference-ktx:${Versions.ANDROIDX_PREFERENCES}"
    const val MATERIAL = "com.google.android.material:material:${Versions.MATERIAL}"

    // Other
    const val AUTH0 = "com.auth0.android:auth0:${Versions.AUTH0}"
    const val CODE_SCANNER = "com.github.yuriy-budiyev:code-scanner:${Versions.CODE_SCANNER}"
    const val GSON = "com.google.code.gson:gson:${Versions.GSON}"
    const val JWTDECODE = "com.auth0.android:jwtdecode:${Versions.JWTDECODE}"
    const val OKHTTP = "com.squareup.okhttp3:okhttp:${Versions.OKHTTP}"
    const val OKHTTP_LOGGING_INTERCEPTOR =
        "com.squareup.okhttp3:logging-interceptor:${Versions.OKHTTP}"
    const val OKTA_IDX_JAVA_API = "com.okta.idx.sdk:okta-idx-java-api:${Versions.OKTA_IDX_JAVA_API}"
    const val OKTA_JWT_VERIFIER = "com.okta.jwt:okta-jwt-verifier:${Versions.OKTA_JWT_VERIFIER}"
    const val OKTA_JWT_VERIFIER_IMPL =
        "com.okta.jwt:okta-jwt-verifier-impl:${Versions.OKTA_JWT_VERIFIER}"
    const val OKTA_OIDC_ANDROID = "com.okta.android:okta-oidc-android:${Versions.OKTA_OIDC_ANDROID}"
    const val RETROFIT = "com.squareup.retrofit2:retrofit:${Versions.RETROFIT}"
    const val RETROFIT_CONVERTER_GSON = "com.squareup.retrofit2:converter-gson:${Versions.RETROFIT}"
    const val TIMBER = "com.jakewharton.timber:timber:${Versions.TIMBER}"
    const val ZXING_EMBEDDED = "com.journeyapps:zxing-android-embedded:${Versions.ZXING_EMBEDDED}"

    // ByndId
    const val CORE = "com.beyondidentity.android.sdk:core"
    const val DEVICE_INFO = "com.beyondidentity.android.sdk:device-info"
    const val ENCLAVE = "com.beyondidentity.android.sdk:enclave"
    const val LOG = "com.beyondidentity.android.sdk:log"
    const val OPTICS = "com.beyondidentity.android.sdk:optics"
    const val PRELUDE = "com.beyondidentity.android.sdk:prelude"
    const val PROTO = "com.beyondidentity.android.sdk:proto"
}

object DebugLibs {
    const val ANDROIDX_COMPOSE_UI_TOOLING =
        "androidx.compose.ui:ui-tooling:${Versions.ANDROIDX_COMPOSE}"
}

object TestLibs {
    const val ANDROIDX_TEST_CORE = "androidx.test:core:${Versions.ANDROIDX_TEST}"
    const val JUNIT = "junit:junit:${Versions.JUNIT}"
    const val KOTLINX_COROUTINES_TEST =
        "org.jetbrains.kotlinx:kotlinx-coroutines-test:${Versions.KOTLINX_COROUTINES}"
    const val ROBOLECTRIC = "org.robolectric:robolectric:${Versions.ROBOLECTRIC}"
}

object AndroidTestLibs {
    const val ANDROIDX_ESPRESSO_CORE =
        "androidx.test.espresso:espresso-core:${Versions.ANDROIDX_ESPRESSO}"
    const val ANDROIDX_EXT_JUNIT = "androidx.test.ext:junit:${Versions.ANDROIDX_EXT_JUNIT}"
    const val ANDROIDX_RULES = "androidx.test:rules:${Versions.ANDROIDX_TEST}"
    const val ANDROIDX_RUNNER = "androidx.test:runner:${Versions.ANDROIDX_TEST}"
    const val MOCKITO_ANDROID = "org.mockito:mockito-android:${Versions.MOCKITO}"
    const val MOCKITO_CORE = "org.mockito:mockito-core:${Versions.MOCKITO}"
}

object Classpaths {
    const val ANDROID_GRADLE_PLUGIN =
        "com.android.tools.build:gradle:${Versions.ANDROID_GRADLE_PLUGIN}"
    const val DOKKA_GRADLE_PLUGIN = "org.jetbrains.dokka:dokka-gradle-plugin:${Versions.DOKKA}"
    const val KOTLIN_GRADLE_PLUGIN = "org.jetbrains.kotlin:kotlin-gradle-plugin:${Versions.KOTLIN}"
    const val KOTLIN_SERIALIZATION_PLUGIN =
        "org.jetbrains.kotlin:kotlin-serialization:${Versions.KOTLIN}"
    const val KTLINT_GRADLE_PLUGIN = "org.jlleitschuh.gradle:ktlint-gradle:${Versions.KTLINT}"
}

object Modules {
    const val EMBEDDED = ":embedded"
    const val EMBEDDED_UI = ":embedded-ui"
}
