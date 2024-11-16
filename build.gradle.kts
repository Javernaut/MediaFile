// The gradle-nexus.publish-plugin considers the Project.version to pick the repository url (staging or snapshot)
apply(from = "${rootDir}/scripts/read-arguments.gradle")

plugins {
    alias(libs.plugins.android.library) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.publishPlugin)
}

// TODO Extract this part to a separate place
group = "io.github.javernaut"
version = "2.0.0" + rootProject.ext["versionSuffix"]

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
