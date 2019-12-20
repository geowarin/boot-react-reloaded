package com.geowarin.bootreact.api

import org.springframework.http.HttpStatus
import org.springframework.security.authentication.ReactiveAuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.web.server.context.ServerSecurityContextRepository
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import org.springframework.web.reactive.function.server.bodyToMono
import reactor.core.publisher.Mono

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
        val token = UsernamePasswordAuthenticationToken(
          creds.userName,
          creds.password
        )
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
      .flatMap { ServerResponse.ok().bodyValue("Auth success") }
      .onErrorResume {
        ServerResponse.status(HttpStatus.UNAUTHORIZED).bodyValue(it.message ?: "Unauthorized")
      }
  }
}
