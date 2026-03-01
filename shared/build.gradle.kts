plugins {
    kotlin("multiplatform")
    kotlin("native.cocoapods")
    id("com.android.library")
}

kotlin {
    androidTarget {
        compilerOptions {
            jvmTarget.set(org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_11)
        }
    }

    iosX64()
    iosArm64()
    iosSimulatorArm64()

    cocoapods {
        summary = "WaterReminder Shared Module"
        homepage = "https://github.com/nizamiqarayev/WaterReminderApp"
        version = "1.0"
        ios.deploymentTarget = "14.0"
        framework {
            baseName = "shared"
        }
        pod("FirebaseCore") { version = ">= 10.0" }
        pod("FirebaseAuth") { version = ">= 10.0" }
        pod("FirebaseFirestore") { version = ">= 10.0" }
    }

    targets.withType<org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget> {
        compilations.getByName("main").cinterops.all {
            extraOpts("-Xcc", "-fno-modules")
        }
    }

    sourceSets {
        commonMain.dependencies {
            implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.10.1")
            implementation("dev.gitlive:firebase-auth:2.1.0")
            implementation("dev.gitlive:firebase-firestore:2.1.0")
        }
        androidMain.dependencies {
            implementation("com.google.firebase:firebase-auth:22.3.1")
            implementation("com.google.firebase:firebase-firestore:24.11.0")
        }
        // iosMain is now auto-created by the default hierarchy template
        // Put iOS-specific code in: shared/src/iosMain/kotlin/
    }
}

android {
    namespace = "com.example.waterreminder.shared"
    compileSdk = 35
    defaultConfig {
        minSdk = 24
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}