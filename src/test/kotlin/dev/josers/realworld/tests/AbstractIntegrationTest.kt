package dev.josers.realworld.tests

import dev.josers.realworld.config.KGenericContainer
import dev.josers.realworld.config.TestConfig
import org.junit.jupiter.api.TestInstance
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.web.server.LocalServerPort
import org.springframework.context.ApplicationContextInitializer
import org.springframework.context.ConfigurableApplicationContext
import org.springframework.context.annotation.Import
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.support.TestPropertySourceUtils.addInlinedPropertiesToEnvironment
import org.testcontainers.junit.jupiter.Container

@ActiveProfiles("test")
@Import(TestConfig::class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@ContextConfiguration(
    initializers = [
        AbstractIntegrationTest.Companion.MongoInitializer::class
    ]
)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
abstract class AbstractIntegrationTest {

    @LocalServerPort
    var port: Int = 0

    companion object {
        @Container
        @JvmField
        val container = KGenericContainer("mongo:latest").apply {
            withExposedPorts(27017)
        }

        init {
            container.start()
        }

        class MongoInitializer : ApplicationContextInitializer<ConfigurableApplicationContext> {
            override fun initialize(context: ConfigurableApplicationContext) {
                addInlinedPropertiesToEnvironment(
                    context,
                    "realworld.database-url=${container.containerIpAddress}:${container.getMappedPort(27017)}"
                )
            }
        }
    }
}