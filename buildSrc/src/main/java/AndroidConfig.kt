import org.gradle.api.JavaVersion

object AndroidConfig {
    const val BUILD_TOOLS_VERSION = "31.0.0"
    const val COMPILE_SDK_VERSION = 31
    const val MIN_SDK_VERSION = 28
    const val TARGET_SDK_VERSION = 31
    const val NDK_VERSION = "22.0.7026061"
    val JAVA_VERSION = JavaVersion.VERSION_11
}
