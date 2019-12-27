package com.geowarin.bootreact.api

import kotlinx.coroutines.reactive.awaitFirstOrNull
import kotlinx.coroutines.reactive.awaitSingle
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
import org.springframework.web.reactive.function.server.awaitBody
import org.springframework.web.reactive.function.server.bodyValueAndAwait
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

  suspend fun authenticate(credentials: Credentials, serverRequest: ServerRequest): WebSession {
    val token = UsernamePasswordAuthenticationToken(credentials.userName, credentials.password)
    val authentication = authenticationManager.authenticate(token).awaitSingle()
    saveAuthentication(serverRequest, authentication)
    return serverRequest.session().awaitSingle()
  }

  private suspend fun saveAuthentication(serverRequest: ServerRequest, authentication: Authentication) {
    val context = SecurityContextHolder.getContext()
    context.authentication = authentication
    securityContextRepository.save(serverRequest.exchange(), context).awaitFirstOrNull()
  }

  suspend fun logout(serverRequest: ServerRequest) =
    securityContextRepository.save(serverRequest.exchange(), null).awaitFirstOrNull()
}

data class AuthResponse(
  val sessionId: String
)

@Component
class AuthHandlers(val authService: AuthService) {

  suspend fun login(serverRequest: ServerRequest): ServerResponse = try {
    val credentials = serverRequest.awaitBody<Credentials>()
    val session = authService.authenticate(credentials, serverRequest)
    ok().bodyValueAndAwait(AuthResponse(session.id))
  } catch (e: Exception) {
    unauthorized().bodyValueAndAwait(e.message ?: "Unauthorized")
  }

  suspend fun logout(serverRequest: ServerRequest): ServerResponse {
    authService.logout(serverRequest)
    return ok().bodyValueAndAwait("ok")
  }
}

fun unauthorized() = ServerResponse.status(HttpStatus.UNAUTHORIZED)
