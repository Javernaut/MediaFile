// The gradle-nexus.publish-plugin considers the Project.version to pick the repository url (staging or snapshot)
apply(from = "${rootDir}/scripts/publish-root.gradle")

group = ext["PUBLISH_GROUP_ID"] as String
version = ext["PUBLISH_VERSION"] as String

plugins {
    alias(libs.plugins.android.library) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.publishPlugin)
}

// Set up Sonatype repository
nexusPublishing {
    repositories {
        sonatype {
            stagingProfileId = rootProject.ext["sonatypeStagingProfileId"] as String
            username = rootProject.ext["ossrhUsername"] as String
            password = rootProject.ext["ossrhPassword"] as String
            nexusUrl.set(uri("https://s01.oss.sonatype.org/service/local/"))
            snapshotRepositoryUrl.set(uri("https://s01.oss.sonatype.org/content/repositories/snapshots/"))
        }
    }
}
