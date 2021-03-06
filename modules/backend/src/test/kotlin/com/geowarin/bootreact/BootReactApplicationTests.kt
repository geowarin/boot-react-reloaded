package com.geowarin.bootreact

import com.geowarin.bootreact.api.Credentials
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.web.reactive.server.EntityExchangeResult
import org.springframework.test.web.reactive.server.WebTestClient

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@AutoConfigureWebTestClient(timeout = "1h")
class BootReactApplicationTests(@Autowired val client: WebTestClient) {

  @Test
  fun `auth error`() {
    client.post().uri("/api/auth")
      .bodyValue(Credentials("toto", "toto"))
      .accept(MediaType.APPLICATION_JSON)
      .exchange()
      .expectStatus().isUnauthorized
  }

  @Test
  fun `auth success`() {
    val authResult: EntityExchangeResult<ByteArray> = client.post().uri("/api/auth")
      .bodyValue(Credentials("admin", "admin"))
      .accept(MediaType.APPLICATION_JSON)
      .exchange()
      .expectStatus().isOk
      .expectHeader().exists("Set-Cookie")
      .expectHeader().valueMatches("Set-Cookie", "SESSION=.*")
      .expectBody()
			.jsonPath("\$.sessionId").isNotEmpty
      .returnResult()

    val sessionId = authResult.getCookieValue("SESSION")

    client.get().uri("/api/whatever")
      .accept(MediaType.APPLICATION_JSON)
      .cookie("SESSION", sessionId)
      .exchange()
      .expectStatus().isOk
      .expectBody()
      .jsonPath("\$.message").isNotEmpty
  }
}

private fun EntityExchangeResult<*>.getCookieValue(cookieName: String): String {
  val sessionCookie = responseCookies[cookieName]?.first()
    ?: throw IllegalStateException()
  return sessionCookie.value
}

