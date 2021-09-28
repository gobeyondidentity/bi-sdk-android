object Versions {
    const val KOTLIN = "1.4.31"
    const val KOTLINX_SERIALIZATION = "1.0.1"
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

    // Android Test
    const val ANDROIDX_EXT_JUNIT = "1.1.2"
    const val ANDROIDX_ESPRESSO_CORE = "3.3.0"

    // Classpath
    const val ANDROID_GRADLE_TOOLS = "7.0.2"
    const val KTLINT = "10.0.0"
    const val DOKKA = "1.5.0"
}

object Libs {
    // Kotlin
    const val KOTLIN_STD_LIB = "org.jetbrains.kotlin:kotlin-stdlib-jdk8:${Versions.KOTLIN}"
    const val KOTLINX_SERIALIZATION =
        "org.jetbrains.kotlinx:kotlinx-serialization-json:${Versions.KOTLINX_SERIALIZATION}"

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
}

object AndroidTestLibs {
    const val ANDROIDX_EXT_JUNIT = "androidx.test.ext:junit:${Versions.ANDROIDX_EXT_JUNIT}"
    const val ANDROIDX_ESPRESSO_CORE =
        "androidx.test.espresso:espresso-core:${Versions.ANDROIDX_ESPRESSO_CORE}"
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
