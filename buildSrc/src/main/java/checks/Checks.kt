package checks

import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.jlleitschuh.gradle.ktlint.reporter.ReporterType.HTML

fun Project.ktlintCheckConfig() {
    configure<org.jlleitschuh.gradle.ktlint.KtlintExtension> {
        android.set(false)
        outputToConsole.set(true)
        verbose.set(true)
        outputColorName.set("RED")
        ignoreFailures.set(false)
        reporters {
            reporter(HTML)
        }
    }
}
