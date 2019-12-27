package com.geowarin.bootreact.api

import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse.ok
import org.springframework.web.reactive.function.server.bodyValueAndAwait

data class JsonResource(
  val message: String
)

@Component
class ApiHandlers {

  suspend fun whatever(serverRequest: ServerRequest) =
    ok().bodyValueAndAwait(JsonResource("Hello world"))
}
