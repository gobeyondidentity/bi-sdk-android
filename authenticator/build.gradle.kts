import config.configureAndroidLib
import config.configureMavenPublish

plugins {
    id("com.android.library")
    id("kotlin-android")
    id("maven-publish")
}

configureAndroidLib()

configureMavenPublish(
    groupIdForLib = "com.beyondidentity.android.sdk",
    artifactIdForLib = "authenticator",
    versionForLib = "0.0.0-alpha06"
)

dependencies {
    implementation(Libs.KOTLIN_STD_LIB)
    implementation(Libs.ANDROIDX_BROWSER)
    implementation(Libs.ANDROIDX_CONSTRAINT_LAYOUT)

    testImplementation(TestLibs.JUNIT)

    androidTestImplementation(AndroidTestLibs.ANDROIDX_EXT_JUNIT)
    androidTestImplementation(AndroidTestLibs.ANDROIDX_ESPRESSO_CORE)
}