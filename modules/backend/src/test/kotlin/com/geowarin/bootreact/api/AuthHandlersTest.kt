package com.geowarin.bootreact.api

import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Test
import org.springframework.http.HttpStatus
import org.springframework.mock.http.server.reactive.MockServerHttpRequest
import org.springframework.mock.web.reactive.function.server.MockServerRequest
import org.springframework.mock.web.server.MockServerWebExchange
import org.springframework.mock.web.server.MockWebSession
import org.springframework.security.authentication.ReactiveAuthenticationManager
import org.springframework.security.web.server.context.WebSessionServerSecurityContextRepository
import reactor.core.publisher.Mono
import kotlin.test.assertEquals

internal class AuthHandlersTest {

  @Test
  fun testAuth() {
    val authManager = ReactiveAuthenticationManager { a -> Mono.just(a) }
    val securityContextRepository = WebSessionServerSecurityContextRepository()
    val authService = AuthService(authManager, securityContextRepository)

    val session = MockWebSession()
    val exchange = MockServerWebExchange
      .builder(MockServerHttpRequest.post("/blabla"))
      .session(session)
      .build()
    val serverRequest = MockServerRequest.builder()
      .exchange(exchange)
      .session(session)
      .body(Mono.just(Credentials("user", "pass")))

    runBlocking {
      val serverResponse = AuthHandlers(authService).login(serverRequest)
      assertEquals(HttpStatus.OK, serverResponse.statusCode())
    }
  }
}
