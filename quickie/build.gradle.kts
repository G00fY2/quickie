plugins {
  alias(libs.plugins.android.library)
  alias(libs.plugins.kotlin.android)
  alias(libs.plugins.kotlin.parcelize)
  alias(libs.plugins.kotlin.dokka)
  `maven-publish`
  signing
}

android {
  namespace = "io.github.g00fy2.quickie"
  resourcePrefix = "quickie"
  buildFeatures {
    viewBinding = true
  }
  flavorDimensions += "mlkit"
  productFlavors {
    create("bundled").dimension = "mlkit"
    create("unbundled").dimension = "mlkit"
  }
  sourceSets {
    getByName("bundled").java.srcDirs("src/bundled/kotlin")
    getByName("unbundled").java.srcDirs("src/unbundled/kotlin")
  }
  publishing {
    singleVariant("bundledRelease")
    singleVariant("unbundledRelease")
  }
}

dependencies {
  implementation(libs.androidx.appcompat)
  implementation(libs.androidx.core)

  implementation(libs.androidx.camera)
  implementation(libs.androidx.cameraLifecycle)
  implementation(libs.androidx.cameraPreview)

  add("bundledImplementation", libs.mlkit.barcodeScanning)
  add("unbundledImplementation", libs.mlkit.barcodeScanningGms)

  testImplementation(libs.test.junit)
  testRuntimeOnly(libs.test.junit.platformLauncher)
}

group = "io.github.g00fy2.quickie"
version = libs.versions.quickie.get()

tasks.register<Jar>("androidJavadocJar") {
  archiveClassifier = "javadoc"
  from(layout.buildDirectory.dir("dokka/javadoc"))
  dependsOn("dokkaJavadoc")
}

tasks.register<Jar>("androidBundledSourcesJar") {
  archiveClassifier = "sources"
  from(android.sourceSets.getByName("main").java.srcDirs, android.sourceSets.getByName("bundled").java.srcDirs)
}

tasks.register<Jar>("androidUnbundledSourcesJar") {
  archiveClassifier = "sources"
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
  artifact(tasks.named("android${flavor.replaceFirstChar { it.titlecase() }}SourcesJar"))
  pom {
    name = "quickie-$flavor"
    description = "Android QR code scanning library"
    url = "https://github.com/G00fY2/quickie"
    licenses {
      license {
        name = "MIT License"
        url = "https://opensource.org/licenses/MIT"
      }
    }
    developers {
      developer {
        id = "g00fy2"
        name = "Thomas Wirth"
        email = "twirth.development@gmail.com"
      }
    }
    scm {
      connection = "https://github.com/G00fY2/quickie.git"
      developerConnection = "https://github.com/G00fY2/quickie.git"
      url = "https://github.com/G00fY2/quickie"
    }
  }
}

fun Project.findStringProperty(propertyName: String): String? {
  return findProperty(propertyName) as String? ?: run {
    println("$propertyName missing in gradle.properties")
    null
  }
}