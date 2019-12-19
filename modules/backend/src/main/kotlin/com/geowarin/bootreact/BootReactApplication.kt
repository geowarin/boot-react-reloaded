package com.geowarin.bootreact

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpStatus
import org.springframework.security.authentication.ReactiveAuthenticationManager
import org.springframework.security.authentication.UserDetailsRepositoryReactiveAuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.config.web.server.ServerHttpSecurity
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.MapReactiveUserDetailsService
import org.springframework.security.core.userdetails.ReactiveUserDetailsService
import org.springframework.security.core.userdetails.User
import org.springframework.security.web.server.SecurityWebFilterChain
import org.springframework.security.web.server.authentication.HttpStatusServerEntryPoint
import org.springframework.security.web.server.context.ServerSecurityContextRepository
import org.springframework.security.web.server.context.WebSessionServerSecurityContextRepository
import org.springframework.stereotype.Component
import org.springframework.web.cors.CorsConfiguration
import org.springframework.web.cors.reactive.CorsConfigurationSource
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import org.springframework.web.reactive.function.server.bodyToMono
import org.springframework.web.reactive.function.server.router
import reactor.core.publisher.Hooks
import reactor.core.publisher.Mono


@SpringBootApplication
class BootReactApplication

fun main(args: Array<String>) {
  if (!isRestart()) {
    startFrontend()
  }
  Hooks.onOperatorDebug();
  runApplication<BootReactApplication>(*args)
}

private fun isRestart() = Thread.currentThread().name == "restartedMain"

data class JsonResource(
  val message: String
)

@Configuration
class RouteConfig(
  val authHandlers: AuthHandlers
) {
  @Bean
  fun routes() = router {
    path("/api").nest {
      GET("/auth") {
        ServerResponse.ok().bodyValue(JsonResource("lol"))
      }
      POST("/auth", authHandlers::auth)
      GET("/toto") {
        ServerResponse.ok().bodyValue(JsonResource("Hello world"))
      }
    }
  }
}

data class Credentials(
  val userName: String,
  val password: String
)

@Component
class AuthHandlers(
  val authenticationManager: ReactiveAuthenticationManager,
  val securityContextRepository: ServerSecurityContextRepository
) {
  fun auth(serverRequest: ServerRequest): Mono<ServerResponse> {
    return serverRequest.bodyToMono<Credentials>()
      .flatMap { creds ->
        val token = UsernamePasswordAuthenticationToken(creds.userName, creds.password)
        authenticationManager.authenticate(token)
      }
      .map { auth ->
        serverRequest.session()
          .map { it.save() }
          .`as` { auth }
      }
      .flatMap { authentication ->
        val context = SecurityContextHolder.getContext()
        context.authentication = authentication
        securityContextRepository.save(serverRequest.exchange(), context)
      }
      .flatMap { ServerResponse.ok().bodyValue(JsonResource("ok")) }
  }
}

@Configuration
class SecurityConfig {

  @Bean
  fun securityWebFilterChain(http: ServerHttpSecurity): SecurityWebFilterChain {
    return http.authorizeExchange()
//      .anyExchange().permitAll()
      .pathMatchers("/api/auth").permitAll()
      .pathMatchers("/api/**").authenticated()
      .and().csrf().disable()
      // Disable HttpBasicServerAuthenticationEntryPoint which appends the WWW-Authenticate header
      .exceptionHandling()
      .authenticationEntryPoint(HttpStatusServerEntryPoint(HttpStatus.UNAUTHORIZED)).and()
      // mmmm
      .securityContextRepository(securityContextRepository())
//      .authenticationManager(UserDetailsRepositoryReactiveAuthenticationManager(userDetailsService()))
      .build()
  }

  @Bean
  fun securityContextRepository() = WebSessionServerSecurityContextRepository()

  @Bean
  fun authManager(userDetailsService: ReactiveUserDetailsService): ReactiveAuthenticationManager =
    UserDetailsRepositoryReactiveAuthenticationManager(userDetailsService)

  @Bean
  fun userDetailsService(): ReactiveUserDetailsService = MapReactiveUserDetailsService(
    User("admin", "{noop}admin", emptyList())
  )

  @Bean
  fun corsConfigurationSource(): CorsConfigurationSource {
    val configuration = CorsConfiguration().apply {
      allowedOrigins = listOf("http://localhost:1234")
      allowedMethods = listOf("*")
      allowedHeaders = listOf("*")
      allowCredentials = true
    }
    val source = UrlBasedCorsConfigurationSource()
    source.registerCorsConfiguration("/api/**", configuration)
    return source
  }
}

