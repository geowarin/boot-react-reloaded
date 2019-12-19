package com.geowarin.bootreact.api

import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse

data class JsonResource(
  val message: String
)

@Component
class ApiHandlers {

  fun whatever(serverRequest: ServerRequest) = ServerResponse.ok()
    .bodyValue(JsonResource("Hello world"))
}
