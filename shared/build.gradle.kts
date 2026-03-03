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
        podfile = project.file("../iosApp/Podfile")
        
        framework {
            baseName = "shared"
            isStatic = true
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