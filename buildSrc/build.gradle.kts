plugins {
  `java-gradle-plugin`
  `kotlin-dsl`
}

gradlePlugin {
  plugins {
    register("com.g00fy2.quickie.sample") {
      id = "com.g00fy2.quickie.sample"
      implementationClass = "SampleAppPlugin"
    }
  }
}

kotlinDslPluginOptions {
  experimentalWarning.set(false)
}

repositories {
  google()
  jcenter()
}

dependencies {
  compileOnly(gradleApi())

  implementation("com.android.tools.build:gradle:4.1.1")
  implementation(kotlin("gradle-plugin", "1.4.20"))
}