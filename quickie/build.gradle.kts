plugins {
    id("com.android.library")
    kotlin("android")
}

android {
    compileSdkVersion(30)
    defaultConfig {
        minSdkVersion(21)
        consumerProguardFiles("consumer-rules.pro")
    }
    buildFeatures {
        viewBinding = true
    }
    sourceSets {
        getByName("main").java.srcDirs("src/main/kotlin")
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = JavaVersion.VERSION_1_8.toString()
    }
}

repositories {
    google()
    mavenCentral()
    jcenter {
        content {
            includeModule("org.jetbrains.trove4j", "trove4j") // required by com.android.tools.lint:lint-gradle
        }
    }
}

dependencies {
    implementation("androidx.appcompat:appcompat:1.3.0-alpha02")

    implementation("androidx.camera:camera-camera2:1.0.0-beta12")
    implementation("androidx.camera:camera-lifecycle:1.0.0-beta12")
    implementation("androidx.camera:camera-view:1.0.0-alpha19")

    implementation("com.google.android.gms:play-services-mlkit-barcode-scanning:16.1.2")
}