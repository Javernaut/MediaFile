plugins {
    alias(libs.plugins.android.library) apply false
    alias(libs.plugins.publishPlugin) apply false
}

buildscript {
    dependencies {
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:${libs.versions.kotlin.get()}")
    }
}
