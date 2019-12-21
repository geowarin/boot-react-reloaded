package com.geowarin.bootreact

import com.geowarin.bootreact.config.CorsProperties
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.runApplication


@SpringBootApplication
@EnableConfigurationProperties(CorsProperties::class)
class BootReactApplication

fun main(args: Array<String>) {
//  Hooks.onOperatorDebug()
  runApplication<BootReactApplication>(*args)
}

