plugins {
  id(Plugins.Android.library)
  id(Plugins.Kotlin.android)
  id(Plugins.Kotlin.parcelize)
  id(Plugins.Kotlin.dokka) version Versions.dokka
  `maven-publish`
  signing
}

android {
  compileSdkVersion(Versions.androidCompileSdk)
  defaultConfig.minSdkVersion(Versions.androidMinSdk)
  resourcePrefix = "quickie"
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
    getByName("test").java.srcDirs("src/test/kotlin")
  }
}

dependencies {
  implementation(Deps.AndroidX.appcompat)

  implementation(Deps.AndroidX.camera)
  implementation(Deps.AndroidX.cameraLifecycle)
  implementation(Deps.AndroidX.cameraPreview)

  "bundledImplementation"(Deps.MLKit.barcodeScanning)
  "unbundledImplementation"(Deps.MLKit.barcodeScanningGms)

  testImplementation(Deps.Test.junitApi)
  testRuntimeOnly(Deps.Test.junitEngine)
}

group = "io.github.g00fy2.quickie"
version = "1.2.0-rc02"

tasks.register<Jar>("androidJavadocJar") {
  archiveClassifier.set("javadoc")
  from("$buildDir/dokka/javadoc")
  dependsOn("dokkaJavadoc")
}

tasks.register<Jar>("androidBundledSourcesJar") {
  archiveClassifier.set("sources")
  from(android.sourceSets.getByName("main").java.srcDirs, android.sourceSets.getByName("bundled").java.srcDirs)
}

tasks.register<Jar>("androidUnbundledSourcesJar") {
  archiveClassifier.set("sources")
  from(android.sourceSets.getByName("main").java.srcDirs, android.sourceSets.getByName("unbundled").java.srcDirs)
}

afterEvaluate {
  tasks.withType<Test>().configureEach {
    useJUnitPlatform()
    testLogging.events("failed", "passed", "skipped")
    enabled = name.endsWith("DebugUnitTest")
  }

  publishing {
    publications {
      create<MavenPublication>("bundledRelease") { commonConfig("bundled") }
      create<MavenPublication>("unbundledRelease") { commonConfig("unbundled") }
    }
    repositories {
      maven {
        name = "sonatype"
        url = uri("https://s01.oss.sonatype.org/service/local/staging/deploy/maven2/")
        credentials {
          username = findStringProperty("sonatypeUsername")
          password = findStringProperty("sonatypePassword")
        }
      }
    }
  }
}

signing {
  findStringProperty("signing.keyId")
  findStringProperty("signing.password")
  findStringProperty("signing.secretKeyRingFile")
  sign(publishing.publications)
}

fun MavenPublication.commonConfig(flavor: String) {
  from(components["${flavor}Release"])
  artifactId = "quickie-$flavor"
  artifact(tasks.named("androidJavadocJar"))
  artifact(tasks.named("android${flavor.capitalize()}SourcesJar"))
  pom {
    name.set("quickie-$flavor")
    description.set("Android QR code scanning library")
    url.set("https://github.com/G00fY2/quickie")
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
      connection.set("https://github.com/G00fY2/quickie.git")
      developerConnection.set("https://github.com/G00fY2/quickie.git")
      url.set("https://github.com/G00fY2/quickie")
    }
  }
}

fun Project.findStringProperty(propertyName: String): String? {
  return findProperty(propertyName) as String? ?: run {
    println("$propertyName missing in gradle.properties")
    null
  }
}