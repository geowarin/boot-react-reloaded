package com.geowarin.bootreact

import com.geowarin.bootreact.api.ApiHandlers
import com.geowarin.bootreact.api.AuthHandlers
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.reactive.function.server.router
import reactor.core.publisher.Mono


@Configuration
class Router(
  val authHandlers: AuthHandlers,
  val apiHandlers: ApiHandlers
) {
  @Bean
  fun routes() = router {
    //  and contentType(MediaType.APPLICATION_JSON) CRSF protection
    path("/api").nest {
      POST("/auth", authHandlers::login)
      DELETE("/auth", authHandlers::logout)
      GET("/whatever", apiHandlers::whatever)
    }
    GET("/test") {
      Mono.empty<Void>()
        .flatMap { ok().bodyValue("toto") }
    }
  }
}
