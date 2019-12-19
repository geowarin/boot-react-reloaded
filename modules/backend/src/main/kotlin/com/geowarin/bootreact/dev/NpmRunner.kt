package com.geowarin.bootreact.dev

import org.slf4j.LoggerFactory
import org.zeroturnaround.exec.ProcessExecutor
import org.zeroturnaround.exec.StartedProcess
import org.zeroturnaround.exec.stream.slf4j.Slf4jStream
import java.io.File

val webDir = File("./modules/frontend")

fun startFrontend() {
  if (!npmCheckDependencies()) {
    npmInstall()
  }
  npmStart()
}

fun npmCheckDependencies(): Boolean {
  val process = ProcessExecutor()
    .cmd("npm", "run", "check-dependencies")
    .directory(webDir)
    .exitValueAny()

  return process.execute().exitValue == 0
}

fun npmStart(): StartedProcess {
  return ProcessExecutor()
    .cmd("npm", "start")
    .directory(webDir)
    .redirectOutput(Slf4jStream.of(LoggerFactory.getLogger("npm")).asInfo())
    .redirectError(Slf4jStream.of(LoggerFactory.getLogger("npm")).asError()).start()
}

fun ProcessExecutor.cmd(vararg args: String): ProcessExecutor {
  if (isWindows()) {
    return command("cmd", "/c", *args)
  }
  return command(*args)
}

fun isWindows() = System.getProperty("os.name").toLowerCase().indexOf("win") >= 0

fun npmInstall() {
  ProcessExecutor()
    .cmd("npm", "install")
    .directory(webDir)
    .redirectOutput(Slf4jStream.of(LoggerFactory.getLogger("npm")).asInfo())
    .redirectError(Slf4jStream.of(LoggerFactory.getLogger("npm")).asError())
    .exitValueNormal()
    .execute()
}
