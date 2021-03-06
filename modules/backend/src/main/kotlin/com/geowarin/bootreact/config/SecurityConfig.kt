package com.geowarin.bootreact.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpStatus
import org.springframework.security.authentication.ReactiveAuthenticationManager
import org.springframework.security.authentication.UserDetailsRepositoryReactiveAuthenticationManager
import org.springframework.security.config.web.server.ServerHttpSecurity
import org.springframework.security.core.userdetails.MapReactiveUserDetailsService
import org.springframework.security.core.userdetails.ReactiveUserDetailsService
import org.springframework.security.core.userdetails.User
import org.springframework.security.web.server.SecurityWebFilterChain
import org.springframework.security.web.server.authentication.HttpStatusServerEntryPoint
import org.springframework.security.web.server.context.WebSessionServerSecurityContextRepository
import org.springframework.web.cors.CorsConfiguration
import org.springframework.web.cors.reactive.CorsConfigurationSource
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource

@Configuration
class SecurityConfig {

  @Bean
  fun securityWebFilterChain(http: ServerHttpSecurity): SecurityWebFilterChain {
    return http.authorizeExchange()
      .pathMatchers("/api/auth").permitAll()
      .pathMatchers("/api/**").authenticated()
      .and().csrf().disable()
      // Disable HttpBasicServerAuthenticationEntryPoint which appends the WWW-Authenticate header
      .exceptionHandling()
      .authenticationEntryPoint(HttpStatusServerEntryPoint(HttpStatus.UNAUTHORIZED))
      .and()
      // use web sessions to store and retrieve auth
      .securityContextRepository(securityContextRepository())
      // We handle logout
      .logout().disable()
      .build()
  }

  @Bean
  fun securityContextRepository() = WebSessionServerSecurityContextRepository()

  @Bean
  fun authManager(userDetailsService: ReactiveUserDetailsService): ReactiveAuthenticationManager =
    UserDetailsRepositoryReactiveAuthenticationManager(userDetailsService)

  @Bean
  fun userDetailsService(): ReactiveUserDetailsService =
    MapReactiveUserDetailsService(
      User("admin", "{noop}admin", emptyList())
    )

  @Bean
  fun corsConfigurationSource(corsProperties: CorsProperties): CorsConfigurationSource {
    if (corsProperties.allowedOrigin.isEmpty()) {
      return UrlBasedCorsConfigurationSource()
    }

    val configuration = CorsConfiguration().apply {
      allowedOrigins = corsProperties.allowedOrigin
      allowedMethods = listOf("*")
      allowedHeaders = listOf("*")
      allowCredentials = true
    }
    val source = UrlBasedCorsConfigurationSource()
    source.registerCorsConfiguration("/api/**", configuration)
    return source
  }
}
