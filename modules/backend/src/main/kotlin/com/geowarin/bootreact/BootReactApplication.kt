package com.geowarin.bootreact

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.DependsOn
import org.springframework.core.io.FileSystemResource
import org.springframework.http.MediaType.TEXT_HTML
import org.springframework.web.reactive.function.server.RouterFunctions
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

@Configuration
class RouteConfig {
  @Bean
  fun routes() = router {
    accept(TEXT_HTML).nest {
      GET("/") {
        val model = mapOf<String, Any>(
          "currentPage" to "pages/toto.tsx"
        )
        ServerResponse.ok().render("index", model)
      }
    }
//    	("/api/blog" and accept(APPLICATION_JSON)).nest {
//    		GET("/", barHandler::findAll)
//    		GET("/{id}", barHandler::findOne)
//    	}
  }

  @Bean
  @DependsOn("routes")
  fun resourceRouter() = RouterFunctions.resources("/**", FileSystemResource("modules/frontend/dist/"))

}

