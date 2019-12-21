package com.geowarin.bootreact.config

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding

@ConstructorBinding
@ConfigurationProperties(prefix = "com.geowarin.cors")
data class CorsProperties(
  val allowedOrigin: List<String>
)
