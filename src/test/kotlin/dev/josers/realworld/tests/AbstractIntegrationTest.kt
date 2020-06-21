package dev.josers.realworld.tests

import dev.josers.realworld.config.KGenericContainer
import dev.josers.realworld.config.TestConfig
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.util.TestPropertyValues
import org.springframework.boot.web.server.LocalServerPort
import org.springframework.context.ApplicationContextInitializer
import org.springframework.context.ConfigurableApplicationContext
import org.springframework.context.annotation.Import
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.ContextConfiguration
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.junit.jupiter.Testcontainers

@Testcontainers
@Import(TestConfig::class)
@ActiveProfiles("test")
@ContextConfiguration(initializers = [AbstractIntegrationTest.MongoInitializer::class])
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
abstract class AbstractIntegrationTest {

    @LocalServerPort
    var port: Int = 0

    companion object {
        @Container
        val container = KGenericContainer("mongo:latest").apply {
            withExposedPorts(27017)
        }
    }

    class MongoInitializer: ApplicationContextInitializer<ConfigurableApplicationContext> {
        override fun initialize(configurableApplicationContext: ConfigurableApplicationContext) {
            val values = TestPropertyValues.of(
                "spring.data.mongodb.host=${container.containerIpAddress}",
                "spring.data.mongodb.port=${container.getMappedPort(27017)}"
            )
            values.applyTo(configurableApplicationContext)
        }
    }
}