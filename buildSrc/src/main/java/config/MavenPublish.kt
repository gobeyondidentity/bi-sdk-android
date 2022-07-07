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
                    url = uri(System.getenv().getOrDefault("CLOUDSMITH_PUBLISH_URL", "https://www.example.com"))
                    credentials {
                        username = System.getenv().getOrDefault("CLOUDSMITH_USER", "username")
                        password = System.getenv().getOrDefault("CLOUDSMITH_API_KEY", "password")
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
