package com.geowarin.bootreact

import com.geowarin.bootreact.config.CorsProperties
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Configuration
import org.springframework.data.redis.connection.RedisConnectionFactory
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory


@SpringBootApplication
@EnableConfigurationProperties(CorsProperties::class)
class BootReactApplication

fun main(args: Array<String>) {
//  Hooks.onOperatorDebug()
  runApplication<BootReactApplication>(*args)
}

@Configuration
class RedisConfig {
  fun redisConnectionFactory(): RedisConnectionFactory = LettuceConnectionFactory()
}

//@Configuration
//@EnableSpringWebSession
//class SessionConfig {
//  @Bean
//  fun reactiveSessionRepository(): ReactiveSessionRepository<*> {
//    return ReactiveMapSessionRepository(ConcurrentHashMap())
//  }
//}
