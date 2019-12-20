package com.geowarin.bootreact

import com.geowarin.bootreact.api.Credentials
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.test.web.reactive.server.WebTestClient
import org.springframework.test.web.reactive.server.expectBodyList
import kotlin.test.assertEquals

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
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
		client.post().uri("/api/auth")
			.bodyValue(Credentials("admin", "admin"))
			.accept(MediaType.APPLICATION_JSON)
			.exchange()
			.expectStatus().isOk
			.expectHeader().exists("Set-Cookie")
			.expectHeader().valueMatches("Set-Cookie", "SESSION=.*")
			.expectBody()
//			.jsonPath("\$.sessionId").isNotEmpty

//		assertEquals(
//			"admin",
//			SecurityContextHolder.getContext().authentication.name
//		)
	}

}
