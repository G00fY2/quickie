plugins {
  id(Plugins.Android.library)
  kotlin(Plugins.Kotlin.android)
  kotlin(Plugins.Kotlin.parcelize)
  `maven-publish`
}

android {
  compileSdkVersion(Config.androidCompileSdkVersion)
  defaultConfig {
    minSdkVersion(Config.androidMinSdkVersion)
  }
  resourcePrefix = project.name
  buildFeatures {
    viewBinding = true
    buildConfig = false
  }
  sourceSets {
    getByName("main") {
      java.srcDirs("src/main/kotlin")
    }
  }
  compileOptions {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
  }
  kotlinOptions {
    jvmTarget = JavaVersion.VERSION_1_8.toString()
  }
  flavorDimensions("mlkit")
  productFlavors {
    create("bundled") {
      dimension("mlkit")
    }
    create("unbundled") {
      dimension("mlkit")
    }
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

val bundledImplementation by configurations
val unbundledImplementation by configurations
dependencies {
  implementation(Deps.AndroidX.activity)
  implementation(Deps.AndroidX.fragment)
  implementation(Deps.AndroidX.appcompat)
  implementation(Deps.AndroidX.core)

  implementation(Deps.AndroidX.camera)
  implementation(Deps.AndroidX.cameraLifecycle)
  implementation(Deps.AndroidX.cameraPreview)

  bundledImplementation(Deps.MLKit.barcodeScanning)
  unbundledImplementation(Deps.MLKit.barcodeScanningGms)
}

group = "com.github.g00fy2" // TODO sync final groupId and package names
version = "0.1.0"

// TODO add Dokka and javadocJar task

tasks.register<Jar>("androidSourcesJar") {
  archiveClassifier.set("sources")
  from(android.sourceSets.getByName("main").java.srcDirs)
}

afterEvaluate {
  publishing {
    publications {
      create<MavenPublication>("bundledRelease") {
        from(components["bundledRelease"])
        artifactId = "quickie-bundled"
        artifact(tasks.named("androidSourcesJar"))
        configurePom()
      }
      create<MavenPublication>("unbundledRelease") {
        from(components["unbundledRelease"])
        artifactId = "quickie-unbundled"
        artifact(tasks.named("androidSourcesJar"))
        configurePom()
      }
    }
  }
}

fun MavenPublication.configurePom() {
  pom {
    name.set(project.name)
    description.set("Android QR code scanner library")
    url.set("https://github.com/G00fY2/Quickie")
    licenses {
      license {
        name.set("The MIT License")
        url.set("https://opensource.org/licenses/MIT")
      }
    }
    developers {
      developer {
        id.set("g00fy2")
        name.set("Thomas Wirth")
        email.set("twirth.development@gmail.com")
      }
    }
    scm {
      connection.set("https://github.com/G00fY2/Quickie.git")
      developerConnection.set("https://github.com/G00fY2/Quickie.git")
      url.set("https://github.com/G00fY2/Quickie")
    }
  }
}