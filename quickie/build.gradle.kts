plugins {
  id(Plugins.Android.library)
  id(Plugins.Kotlin.android)
  id(Plugins.Kotlin.parcelize)
  id(Plugins.Kotlin.dokka) version Versions.dokka
  `maven-publish`
}

android {
  compileSdkVersion(Config.androidCompileSdkVersion)
  defaultConfig.minSdkVersion(Config.androidMinSdkVersion)
  resourcePrefix = project.name
  buildFeatures {
    viewBinding = true
    buildConfig = false
  }
  flavorDimensions("mlkit")
  productFlavors {
    create("bundled").dimension("mlkit")
    create("unbundled").dimension("mlkit")
  }
  sourceSets {
    getByName("main").java.srcDirs("src/main/kotlin")
    getByName("bundled").java.srcDirs("src/bundled/kotlin")
    getByName("unbundled").java.srcDirs("src/unbundled/kotlin")
  }
}

repositories {
  google()
  mavenCentral()
  jcenter {
    content {
      includeGroupByRegex("org\\.jetbrains.*")
      includeModule("com.soywiz.korlibs.korte", "korte-jvm")
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

group = "com.g00fy2.quickie"
version = "0.5.1"

tasks.register<Jar>("androidJavadocJar") {
  archiveClassifier.set("javadoc")
  from("$buildDir/dokka/javadoc")
  dependsOn("dokkaJavadoc")
}

tasks.register<Jar>("androidSourcesJar") {
  archiveClassifier.set("sources")
  from(android.sourceSets.getByName("main").java.srcDirs)
}

// JCenter does not support Gradle module metadata
tasks.withType<GenerateModuleMetadata> {
  enabled = false
}

afterEvaluate {
  publishing {
    publications {
      // publishBundledReleasePublicationToBintrayQuickieBundledRepository -Pbintray_user=name -Pbintray_key=key
      create<MavenPublication>("bundledRelease") {
        from(components["bundledRelease"])
        val libraryName = "quickie-bundled"
        artifactId = libraryName
        artifact(tasks.named("androidJavadocJar"))
        artifact(tasks.named("androidSourcesJar"))
        configurePom(libraryName)
      }
      // publishUnbundledReleasePublicationToBintrayQuickieUnbundledRepository -Pbintray_user=name -Pbintray_key=key
      create<MavenPublication>("unbundledRelease") {
        from(components["unbundledRelease"])
        val libraryName = "quickie-unbundled"
        artifactId = libraryName
        artifact(tasks.named("androidJavadocJar"))
        artifact(tasks.named("androidSourcesJar"))
        configurePom(libraryName)
      }
    }
    repositories {
      maven {
        name = "bintrayQuickieBundled"
        url = uri("https://api.bintray.com/maven/g00fy2/maven/quickie-bundled/;publish=1;")
        credentials {
          username = findProperty("bintray_user") as String?
          password = findProperty("bintray_key") as String?
        }
      }
      maven {
        name = "bintrayQuickieUnbundled"
        url = uri("https://api.bintray.com/maven/g00fy2/maven/quickie-unbundled/;publish=1;")
        credentials {
          username = findProperty("bintray_user") as String?
          password = findProperty("bintray_key") as String?
        }
      }
    }
  }
}

fun MavenPublication.configurePom(libraryName: String) {
  pom {
    name.set(libraryName)
    description.set("Android QR code scanner library")
    url.set("https://github.com/G00fY2/Quickie")
    licenses {
      license {
        name.set("MIT License")
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