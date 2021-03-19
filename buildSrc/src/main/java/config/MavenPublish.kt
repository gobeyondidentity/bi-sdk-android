package config

import org.gradle.api.Project
import org.gradle.api.publish.PublishingExtension
import org.gradle.api.publish.maven.MavenPublication
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.get
import org.gradle.kotlin.dsl.register

fun Project.configureMavenPublish(
    groupIdForLib: String,
    artifactIdForLib: String,
    versionForLib: String
) {
    afterEvaluate {
        configure<PublishingExtension> {
            publications {
                // Creates a Maven publication called "release".
                register("release", MavenPublication::class) {
                    // Applies the component for the release build variant.
                    from(components["release"])

                    // Jitpack sets these values based on the repo and the release tags
                    // for some day when we move away from jitpack
                    groupId = groupIdForLib
                    artifactId = artifactIdForLib
                    version = versionForLib
                }
            }
        }
    }
}
