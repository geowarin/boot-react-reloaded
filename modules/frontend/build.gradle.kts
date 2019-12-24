import com.moowork.gradle.node.npm.NpmTask

plugins {
  id("com.github.node-gradle.node") version "2.0.0"
}

buildDir = file("dist")

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

tasks.register<Delete>("clean") {
  delete("dist")
}

tasks.getByName("assemble").dependsOn += tasks.getByName("npmInstall")
