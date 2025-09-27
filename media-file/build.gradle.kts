plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.jetbrains.kotlin.android)
    alias(libs.plugins.publishPlugin)
}

android {
    namespace = "io.github.javernaut.mediafile"
    compileSdk = 36

    defaultConfig {
        minSdk = 21

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")

        ndk {
            abiFilters += listOf("x86", "x86_64", "armeabi-v7a", "arm64-v8a")
        }
        externalNativeBuild {
            cmake {
                // NDK r27-specific solution for compatibility with 16 kb page
                // Once migrated to NDK r28, this flag will not be needed
                arguments += listOf("-DANDROID_SUPPORT_FLEXIBLE_PAGE_SIZES=ON")
            }
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro",
                "consumer-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = JavaVersion.VERSION_17.toString()
    }
    ndkVersion = "28.2.13676358"
    externalNativeBuild {
        cmake {
            path = file("src/main/cpp/CMakeLists.txt")
            version = "4.1.1"
        }
    }
    testOptions {
        managedDevices {
            localDevices {
                create("pixel2api34") {
                    device = "Pixel 2"
                    apiLevel = 34
                    systemImageSource = "aosp"
                }
                // TODO Use this device on CI once the issue is resolved: https://issuetracker.google.com/issues/377321470
                // For now, local testing is enough to confirm 16 kb pages are implemented properly
                create("pixel2api35") {
                    device = "Pixel 2"
                    apiLevel = 35
                    systemImageSource = "google_apis_ps16k"
                }
            }
        }
    }
    // In order to catch the potential minification issues, the instrumentation testing should be done for minified build type
    testBuildType = "release"
}

dependencies {
    // Core library
    androidTestImplementation(libs.androidx.test.core)

    // AndroidJUnitRunner and JUnit Rules
    androidTestImplementation(libs.androidx.test.runner)
    androidTestImplementation(libs.androidx.test.rules)

    // Assertions
    androidTestImplementation(libs.androidx.test.ext.junit)
    androidTestImplementation(libs.androidx.test.ext.truth)
}

mavenPublishing {
    val artifactId = "mediafile"
    val version = project.providers.gradleProperty("mediafile.version").get()
    val versionSuffix = project.providers.gradleProperty("mediafile.versionSuffix").orNull?.let { "-$it" }.orEmpty()

    coordinates(
        "io.github.javernaut",
        artifactId,
        version + versionSuffix
    )

    pom {
        name.set(artifactId)
        description.set("A library for reading the basic media information about video and audio files")
        url.set("https://github.com/Javernaut/MediaFile")
        licenses {
            license {
                name.set("MIT License")
                url.set("https://opensource.org/license/mit")
            }
        }
        developers {
            developer {
                id.set("javernaut")
                name.set("Oleksandr Berezhnyi")
                url.set("https://github.com/Javernaut")
            }
        }
        scm {
            url.set("https://github.com/Javernaut/MediaFile")
            connection.set("scm:git:git://github.com/Javernaut/MediaFile.git")
            developerConnection.set("scm:git:ssh://github.com:Javernaut/MediaFile.git")
        }
    }
}
