import org.gradle.api.JavaVersion

/**
 * @param BUILD_TOOLS_VERSION Specifies the version of the SDK Build Tools to use when building your project.
 * @param COMPILE_SDK_VERSION Specifies the API level to compile your project against.
 * @param MIN_SDK_VERSION The minimum SDK version.
 * @param TARGET_SDK_VERSION The target SDK version.
 * @param NDK_VERSION Requires the specified NDK version to be used.
 * @param JAVA_VERSION Language level of the java source code.
 **/
object AndroidConfig {
    const val BUILD_TOOLS_VERSION = "35.0.0"
    const val COMPILE_SDK_VERSION = 34
    const val MIN_SDK_VERSION = 26
    const val TARGET_SDK_VERSION = 34
    const val NDK_VERSION = "26.1.10909125"
    val JAVA_VERSION = JavaVersion.VERSION_17
}
