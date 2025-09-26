// The gradle-nexus.publish-plugin considers the Project.version to pick the repository url (staging or snapshot)
apply(from = "${rootDir}/scripts/read-arguments.gradle")

plugins {
    alias(libs.plugins.android.library) apply false
    alias(libs.plugins.jetbrains.kotlin.android) apply false
    alias(libs.plugins.publishPlugin) apply false
}
