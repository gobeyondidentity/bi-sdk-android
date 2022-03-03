package config

import org.gradle.api.Project
import org.gradle.api.publish.PublishingExtension
import org.gradle.api.publish.maven.MavenPublication
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.get
import org.gradle.kotlin.dsl.register
import utils.getProp

fun Project.configureMavenPublish(
    groupIdForLib: String,
    artifactIdForLib: String
) {
    val mavenPropPath = "$rootDir/buildProperties/build-maven.properties"
    afterEvaluate {
        configure<PublishingExtension> {
            repositories {
                maven {
                    name = "cloudsmith"
                    url = uri(getProp("BUILD_CONFIG_CLOUDSMITH_PUBLISH_URL", mavenPropPath))
                    credentials {
                        username = getProp("BUILD_CONFIG_CLOUDSMITH_USER", mavenPropPath).toString()
                        password = getProp("BUILD_CONFIG_CLOUDSMITH_API_KEY", mavenPropPath).toString()
                    }
                }
            }

            publications {
                // Creates a Maven publication called "release".
                register("release", MavenPublication::class) {
                    // Applies the component for the release build variant.
                    from(components["release"])

                    groupId = groupIdForLib
                    artifactId = artifactIdForLib
                    version = getProp("BUILD_CONFIG_BI_SDK_VERSION", mavenPropPath).toString()
                }
            }
        }
    }
}
