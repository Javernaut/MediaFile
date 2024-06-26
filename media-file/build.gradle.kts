plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)

    `maven-publish`
    signing
}

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

// TODO Extract this part to a separate place
android {
    publishing {
        singleVariant("release") {
            withSourcesJar()
            withJavadocJar()
        }
    }
}

private val publishArtifactId = "mediafile"

publishing {
    publications {
        create<MavenPublication>("release") {
            groupId = rootProject.group as String
            artifactId = publishArtifactId
            version = rootProject.version as String

            pom {
                name = publishArtifactId
                description =
                    "A library for reading the basic media information about video and audio files"
                url = "https://github.com/Javernaut/MediaFile"
                licenses {
                    license {
                        name = "MIT License"
                        url = "http://www.opensource.org/licenses/mit-license.php"
                    }
                }
                developers {
                    developer {
                        id = "javernaut"
                        name = "Oleksandr Berezhnyi"
                    }
                }
                scm {
                    connection = "scm:git:git://github.com/Javernaut/MediaFile.git"
                    developerConnection = "scm:git:ssh://github.com:Javernaut/MediaFile.git"
                    url = "https://github.com/Javernaut/MediaFile"
                }
            }

            afterEvaluate {
                from(components["release"])
            }
        }
    }
}

signing {
    useInMemoryPgpKeys(
        rootProject.ext["signing.keyId"] as String,
        rootProject.ext["signing.key"] as String,
        rootProject.ext["signing.password"] as String,
    )
    sign(publishing.publications)
}
