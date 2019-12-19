import com.moowork.gradle.node.npm.NpmInstallTask
import com.moowork.gradle.node.npm.NpmTask

plugins {
  id("com.github.node-gradle.node") version "2.0.0"
  idea
}

buildDir = file("dist")

node {
  version = "10.16.3"
  download = true
}

tasks.register<NpmTask>("start") {
  setArgs(listOf("start"))
}

tasks.register<NpmTask>("check") {
  setArgs(listOf("test"))
}

tasks.register<NpmTask>("assemble") {
  inputs.dir("src")
  outputs.dir("dist")
  setArgs(listOf("run", "build"))
}

idea {
  module {
    excludeDirs = setOf(file(".cache"))
  }
}

tasks.getByName("assemble").dependsOn += tasks.getByName("npmInstall")
