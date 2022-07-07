package checks

import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.jlleitschuh.gradle.ktlint.reporter.ReporterType.HTML

fun Project.ktlintCheckConfig() {
    configure<org.jlleitschuh.gradle.ktlint.KtlintExtension> {
        verbose.set(true)
        android.set(false)
        outputToConsole.set(true)
        outputColorName.set("RED")
        ignoreFailures.set(false)
        reporters {
            reporter(HTML)
        }
    }
}

fun Project.ktlintCheckConfigSampleApp() {
    configure<org.jlleitschuh.gradle.ktlint.KtlintExtension> {
        verbose.set(true)
        android.set(false)
        outputToConsole.set(true)
        outputColorName.set("RED")
        ignoreFailures.set(false)
        disabledRules.set(setOf("comment-spacing", "indent"))
        reporters {
            reporter(HTML)
        }
    }
}
