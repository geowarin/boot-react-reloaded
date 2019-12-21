package com.geowarin.bootreact.api

import org.springframework.http.HttpStatus
import org.springframework.security.authentication.ReactiveAuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.web.server.context.ServerSecurityContextRepository
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import org.springframework.web.reactive.function.server.ServerResponse.ok
import org.springframework.web.reactive.function.server.bodyToMono
import org.springframework.web.server.WebSession
import reactor.core.publisher.Mono

data class Credentials(
  val userName: String,
  val password: String
)

@Component
class AuthService(
  val authenticationManager: ReactiveAuthenticationManager,
  val securityContextRepository: ServerSecurityContextRepository
) {

  fun authenticate(credentials: Credentials, serverRequest: ServerRequest): Mono<WebSession> {
    val token = UsernamePasswordAuthenticationToken(credentials.userName, credentials.password)
    return authenticationManager.authenticate(token)
      .flatMap { saveAuthentication(serverRequest, it) }
      .then(serverRequest.session())
  }

  private fun saveAuthentication(serverRequest: ServerRequest, authentication: Authentication): Mono<Void> {
    val context = SecurityContextHolder.getContext()
    context.authentication = authentication
    return securityContextRepository.save(serverRequest.exchange(), context)
  }

  fun logout(serverRequest: ServerRequest): Mono<Void> {
    return securityContextRepository.save(serverRequest.exchange(), null)
  }
}

data class AuthResponse(
  val sessionId: String
)

@Component
class AuthHandlers(val authService: AuthService) {

  fun login(serverRequest: ServerRequest): Mono<ServerResponse> {
    return serverRequest.bodyToMono<Credentials>()
      .flatMap { authService.authenticate(it, serverRequest) }
      .flatMap { ok().bodyValue(AuthResponse(it.id)) }
      .onErrorResume { unauthorized().bodyValue(it.message ?: "Unauthorized") }
  }

  fun logout(serverRequest: ServerRequest): Mono<ServerResponse> {
    return authService.logout(serverRequest)
      .flatMap { ok().bodyValue("ok") }
  }
}

fun unauthorized() = ServerResponse.status(HttpStatus.UNAUTHORIZED)
