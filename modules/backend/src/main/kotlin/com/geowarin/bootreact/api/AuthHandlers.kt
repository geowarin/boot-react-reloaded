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
import reactor.core.publisher.Mono

data class Credentials(
  val userName: String,
  val password: String
)

@Component
class SessionCreator(
  val securityContextRepository: ServerSecurityContextRepository
) {
  fun saveAuthentication(serverRequest: ServerRequest, authentication: Authentication) {
    val context = SecurityContextHolder.getContext()
    context.authentication = authentication
    securityContextRepository.save(serverRequest.exchange(), context)
  }
}

@Component
class AuthHandlers(
  val authenticationManager: ReactiveAuthenticationManager,
  val securityContextRepository: ServerSecurityContextRepository
) {
  fun auth(serverRequest: ServerRequest): Mono<ServerResponse> {
    return serverRequest.bodyToMono<Credentials>()
      .map { UsernamePasswordAuthenticationToken(it.userName, it.password) }
      .flatMap { authenticationManager.authenticate(it) }
      .map { auth ->
        serverRequest.session()
          .map { it.save() }
          .`as` { auth }
      }
      .flatMap { authentication ->
        saveAuthentication(serverRequest, authentication)
      }
      .flatMap { ok().bodyValue("Auth success") }
      .onErrorResume {
        unauthorized().bodyValue(it.message ?: "Unauthorized")
      }
  }

  private fun saveAuthentication(
    serverRequest: ServerRequest,
    authentication: Authentication
  ): Mono<Void> {
    val context = SecurityContextHolder.getContext()
    context.authentication = authentication
    return securityContextRepository.save(serverRequest.exchange(), context)
  }

  fun logout(serverRequest: ServerRequest): Mono<ServerResponse> {
    return securityContextRepository.save(serverRequest.exchange(), null)
      .flatMap { ok().bodyValue("ok") }
  }
}

fun unauthorized() = ServerResponse.status(HttpStatus.UNAUTHORIZED)
