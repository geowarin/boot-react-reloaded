package com.geowarin.bootreact

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.MediaType.APPLICATION_JSON
import org.springframework.security.config.web.server.ServerHttpSecurity
import org.springframework.security.core.userdetails.MapReactiveUserDetailsService
import org.springframework.security.core.userdetails.ReactiveUserDetailsService
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.DelegatingPasswordEncoder
import org.springframework.security.crypto.password.NoOpPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.server.SecurityWebFilterChain
import org.springframework.web.cors.CorsConfiguration
import org.springframework.web.cors.reactive.CorsConfigurationSource
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource
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
class SecurityConfig {

  @Bean
  fun securityWebFilterChain(http: ServerHttpSecurity): SecurityWebFilterChain {
    return http.authorizeExchange()
      .anyExchange().authenticated().and()
      .httpBasic().and()
      .build()
  }

  @Bean
  fun userDetailsService(): ReactiveUserDetailsService {
    return MapReactiveUserDetailsService(
      User("admin", "{noop}admin", emptyList())
    )
  }

  @Bean
  fun corsConfigurationSource(): CorsConfigurationSource {
    val configuration = CorsConfiguration()
    configuration.allowedOrigins = listOf("http://localhost:1234")
    configuration.allowedMethods = listOf("*")
    configuration.allowedHeaders = listOf("*")
    val source = UrlBasedCorsConfigurationSource()
    source.registerCorsConfiguration("/api/**", configuration)
    return source
  }
}

