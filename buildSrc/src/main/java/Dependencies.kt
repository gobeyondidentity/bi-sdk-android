object Versions {
    const val KOTLIN = "1.4.31"
    const val KOTLINX_SERIALIZATION = "1.0.1"
    const val KOTLINX_COROUTINES = "1.4.2"
    const val ANDROIDX_BROWSER = "1.3.0"
    const val ANDROIDX_CONSTRAINT_LAYOUT = "2.0.4"
    const val ANDROIDX_PREFERENCES = "1.1.1"
    const val ANDROIDX_BIOMETRICS = "1.0.1"
    const val ANDROIDX_APPCOMPAT = "1.3.1"
    const val ZXING_EMBEDDED = "4.1.0"
    const val MATERIAL = "1.4.0"
    const val CODE_SCANNER = "2.1.0"

    // Test
    const val JUNIT = "4.13.2"
    const val ANDROIDX_TEST = "1.4.0"
    const val ROBOLECTRIC = "4.6.1"

    // Android Test
    const val ANDROIDX_EXT_JUNIT = "1.1.3"
    const val ANDROIDX_ESPRESSO_CORE = "3.3.0"
    const val MOCKITO_ANDROID_TEST = "3.6.0"

    // Classpath
    const val ANDROID_GRADLE_TOOLS = "7.0.2"
    const val KTLINT = "10.0.0"
    const val DOKKA = "1.5.0"
}

object Libs {
    // Kotlin
    const val KOTLIN_STD_LIB = "org.jetbrains.kotlin:kotlin-stdlib-jdk8:${Versions.KOTLIN}"
    const val KOTLIN_REFLECTION = "org.jetbrains.kotlin:kotlin-reflect:${Versions.KOTLIN}"
    const val KOTLINX_SERIALIZATION =
        "org.jetbrains.kotlinx:kotlinx-serialization-json:${Versions.KOTLINX_SERIALIZATION}"
    const val KOTLINX_COROUTINES_ANDROID =
        "org.jetbrains.kotlinx:kotlinx-coroutines-android:${Versions.KOTLINX_COROUTINES}"

    // Androidx
    const val ANDROIDX_BROWSER = "androidx.browser:browser:${Versions.ANDROIDX_BROWSER}"
    const val ANDROIDX_CONSTRAINT_LAYOUT =
        "androidx.constraintlayout:constraintlayout:${Versions.ANDROIDX_CONSTRAINT_LAYOUT}"
    const val ANDROIDX_PREFERENCES =
        "androidx.preference:preference-ktx:${Versions.ANDROIDX_PREFERENCES}"
    const val ANDROIDX_BIOMETRICS = "androidx.biometric:biometric:${Versions.ANDROIDX_BIOMETRICS}"
    const val MATERIAL = "com.google.android.material:material:${Versions.MATERIAL}"
    const val ANDROIDX_APPCOMAPT = "androidx.appcompat:appcompat:${Versions.ANDROIDX_APPCOMPAT}"

    // Other
    const val ZXING_EMBEDDED = "com.journeyapps:zxing-android-embedded:${Versions.ZXING_EMBEDDED}"
    const val CODE_SCANNER = "com.budiyev.android:code-scanner:${Versions.CODE_SCANNER}"

    // ByndId
    const val CORE = "com.beyondidentity.android.sdk:core"
    const val ENCLAVE = "com.beyondidentity.android.sdk:enclave"
    const val DEVICE_INFO = "com.beyondidentity.android.sdk:device-info"
    const val LOG = "com.beyondidentity.android.sdk:log"
    const val PROTO = "com.beyondidentity.android.sdk:proto"
    const val PRELUDE = "com.beyondidentity.android.sdk:prelude"
    const val OPTICS = "com.beyondidentity.android.sdk:optics"
}

object TestLibs {
    const val JUNIT = "junit:junit:${Versions.JUNIT}"
    const val ANDROIDX_TEST_CORE = "androidx.test:core:${Versions.ANDROIDX_TEST}"
    const val ROBOLECTRIC = "org.robolectric:robolectric:${Versions.ROBOLECTRIC}"
}

object AndroidTestLibs {
    const val ANDROIDX_EXT_JUNIT = "androidx.test.ext:junit:${Versions.ANDROIDX_EXT_JUNIT}"
    const val ANDROIDX_ESPRESSO_CORE =
        "androidx.test.espresso:espresso-core:${Versions.ANDROIDX_ESPRESSO_CORE}"
    const val ANDROIDX_RUNNER = "androidx.test:runner:${Versions.ANDROIDX_TEST}"
    const val ANDROIDX_RULES = "androidx.test:rules:${Versions.ANDROIDX_TEST}"
    const val MOCKITO_CORE = "org.mockito:mockito-core:${Versions.MOCKITO_ANDROID_TEST}"
    const val MOCKITO_ANDROID = "org.mockito:mockito-android:${Versions.MOCKITO_ANDROID_TEST}"
}

object Classpaths {
    const val ANDROID_GRADLE_TOOLS_PLUGIN =
        "com.android.tools.build:gradle:${Versions.ANDROID_GRADLE_TOOLS}"
    const val KOTLIN_GRADLE_PLUGIN = "org.jetbrains.kotlin:kotlin-gradle-plugin:${Versions.KOTLIN}"
    const val KOTLINX_SERIALIZATION_PLUGIN =
        "org.jetbrains.kotlin:kotlin-serialization:${Versions.KOTLIN}"
    const val KTLINT_PLUGIN = "org.jlleitschuh.gradle:ktlint-gradle:${Versions.KTLINT}"
    const val DOKKA_PLUGIN = "org.jetbrains.dokka:dokka-gradle-plugin:${Versions.DOKKA}"
}
