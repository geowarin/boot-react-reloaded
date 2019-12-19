package com.geowarin.bootreact

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.MediaType.APPLICATION_JSON
import org.springframework.web.reactive.config.CorsRegistry
import org.springframework.web.reactive.config.WebFluxConfigurer
import org.springframework.web.reactive.function.server.ServerResponse
import org.springframework.web.reactive.function.server.router


@SpringBootApplication
class BootReactApplication

fun main(args: Array<String>) {
  if (!isRestart()) {
    startFrontend()
  }
  runApplication<BootReactApplication>(*args)
}

private fun isRestart() = Thread.currentThread().name == "restartedMain"

data class JsonResource(
  val message: String
)

@Configuration
class RouteConfig {
  @Bean
  fun routes() = router {
    (GET("/api") and accept(APPLICATION_JSON)).nest {
      GET("/toto") {
        ServerResponse.ok().bodyValue(JsonResource("Hello world"))
      }
    }
  }
}

@Configuration
class CorsGlobalConfiguration : WebFluxConfigurer {
  override fun addCorsMappings(corsRegistry: CorsRegistry) {
    corsRegistry.addMapping("/api/**")
      .allowedOrigins("http://localhost:1234")
  }
}

