plugins {
  alias(libs.plugins.android.library)
  alias(libs.plugins.kotlin.parcelize)
  alias(libs.plugins.kotlin.dokka.javadoc)
  `maven-publish`
  signing
}

android {
  namespace = "io.github.g00fy2.quickie"
  resourcePrefix = "quickie"
  defaultConfig {
    consumerProguardFiles("consumer-proguard-rules.pro")
  }
  buildFeatures {
    viewBinding = true
  }
  flavorDimensions += "mlkit"
  productFlavors {
    create("bundled").dimension = "mlkit"
    create("unbundled").dimension = "mlkit"
  }
  sourceSets {
    getByName("bundled").java.directories.add("src/bundled/kotlin")
    getByName("unbundled").java.directories.add("src/unbundled/kotlin")
  }
  publishing {
    singleVariant("bundledRelease") {
      withSourcesJar()
    }
    singleVariant("unbundledRelease") {
      withSourcesJar()
    }
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

dokka {
  dokkaSourceSets.configureEach {
    suppress.set(name != "bundledRelease")
  }
}

tasks.register<Jar>("androidJavadocJar") {
  archiveClassifier = "javadoc"
  from(tasks.dokkaGeneratePublicationJavadoc.flatMap { it.outputDirectory })
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
        url = uri("https://ossrh-staging-api.central.sonatype.com/service/local/staging/deploy/maven2/")
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