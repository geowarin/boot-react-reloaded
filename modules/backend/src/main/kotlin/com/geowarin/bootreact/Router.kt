package com.geowarin.bootreact

import com.geowarin.bootreact.api.ApiHandlers
import com.geowarin.bootreact.api.AuthHandlers
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.reactive.function.server.router

@Configuration
class Router(
  val authHandlers: AuthHandlers,
  val apiHandlers: ApiHandlers
) {
  @Bean
  fun routes() = router {
    path("/api").nest {
      POST("/auth", authHandlers::auth)
      GET("/whatever", apiHandlers::whatever)
    }
  }
}
