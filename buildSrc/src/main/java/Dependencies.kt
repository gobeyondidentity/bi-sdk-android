object Versions {
    const val KOTLIN = "1.4.31"
    const val ANDROIDX_BROWSER = "1.3.0"
    const val ANDROIDX_CONSTRAINT_LAYOUT = "2.0.4"

    // Test
    const val JUNIT = "4.13.2"

    // Android Test
    const val ANDROIDX_EXT_JUNIT = "1.1.2"
    const val ANDROIDX_ESPRESSO_CORE = "3.3.0"

    // Classpath
    const val ANDROID_GRADLE_TOOLS = "4.1.2"
    const val KTLINT = "10.0.0"
}

object Libs {
    const val KOTLIN_STD_LIB = "org.jetbrains.kotlin:kotlin-stdlib:${Versions.KOTLIN}"
    const val ANDROIDX_BROWSER = "androidx.browser:browser:${Versions.ANDROIDX_BROWSER}"
    const val ANDROIDX_CONSTRAINT_LAYOUT =
        "androidx.constraintlayout:constraintlayout:${Versions.ANDROIDX_CONSTRAINT_LAYOUT}"
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
    const val KTLINT_PLUGIN = "org.jlleitschuh.gradle:ktlint-gradle:${Versions.KTLINT}"
}
