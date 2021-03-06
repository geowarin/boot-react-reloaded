import com.moowork.gradle.node.npm.NpmTask

plugins {
  id("com.github.node-gradle.node") version "2.0.0"
  idea
}

buildDir = file("dist")

node {
  version = "13.3.0"
  download = false
}

tasks.register<NpmTask>("start") {
  setArgs(listOf("start"))
}

tasks.register<NpmTask>("check") {
  setArgs(listOf("test"))
}

tasks.register<NpmTask>("build") {
  inputs.dir("src")
  outputs.dir("dist")
  setArgs(listOf("run", "build"))
}

tasks.register<Delete>("clean") {
  delete("dist", ".cache")
}

idea {
  module {
    excludeDirs = setOf(file(".cache"))
  }
}

tasks.getByName("build").dependsOn += tasks.getByName("npmInstall")
