package dev.josers.realworld

import com.github.javafaker.Faker
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Bean

@TestConfiguration
class TestConfig {
    @Bean
    fun faker() = Faker()
}