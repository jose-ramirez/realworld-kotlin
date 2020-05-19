package dev.josers.realworld

import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean

@SpringBootApplication
class RealworldKotlinApplication {
	companion object {
		@JvmStatic
		fun main(args: Array<String>) {
			runApplication<RealworldKotlinApplication>(*args)
		}
	}

	@Bean
	fun mapper() = ObjectMapper()
}
