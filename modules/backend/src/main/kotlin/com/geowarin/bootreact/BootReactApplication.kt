package com.geowarin.bootreact

import com.geowarin.bootreact.config.CorsProperties
import com.geowarin.bootreact.dev.startFrontend
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.runApplication
import reactor.core.publisher.Hooks


@SpringBootApplication
@EnableConfigurationProperties(CorsProperties::class)
class BootReactApplication

fun main(args: Array<String>) {
  if (!isRestart()) {
    startFrontend()
  }
//  Hooks.onOperatorDebug()
  runApplication<BootReactApplication>(*args)
}

private fun isRestart() = Thread.currentThread().name == "restartedMain"

