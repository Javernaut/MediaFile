plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
}

extra["PUBLISH_ARTIFACT_ID"] = "mediafile"

apply(from = "${rootDir}/scripts/publish-module.gradle")

android {
    namespace = "io.github.javernaut.mediafile"
    compileSdk = 34

    defaultConfig {
        minSdk = 21

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")

        ndk {
            abiFilters += listOf("x86", "x86_64", "armeabi-v7a", "arm64-v8a")
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = JavaVersion.VERSION_1_8.toString()
    }
    ndkVersion = "26.3.11579264"
    externalNativeBuild {
        cmake {
            path = file("CMakeLists.txt")
        }
    }
}

dependencies {
    implementation(libs.androidx.annotation)

    // Core library
    androidTestImplementation(libs.androidx.test.core)

    // AndroidJUnitRunner and JUnit Rules
    androidTestImplementation(libs.androidx.test.runner)
    androidTestImplementation(libs.androidx.test.rules)

    // Assertions
    androidTestImplementation(libs.androidx.test.ext.junit)
    androidTestImplementation(libs.androidx.test.ext.truth)
}
