// The gradle-nexus.publish-plugin considers the Project.version to pick the repository url (staging or snapshot)
apply(from = "${rootDir}/scripts/publish-root.gradle")

group = ext["PUBLISH_GROUP_ID"] as String
version = ext["PUBLISH_VERSION"] as String

plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.publishPlugin)
}
