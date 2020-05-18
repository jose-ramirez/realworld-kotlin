package dev.josers.realworld

import dev.josers.realworld.model.Tag
import dev.josers.realworld.repository.TagRepository
import io.restassured.RestAssured.given
import org.hamcrest.CoreMatchers.equalTo
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.web.server.LocalServerPort
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class TagModuleIntegrationTests {
    @LocalServerPort
    var port: Int = 0

    @Autowired
    lateinit var tagRepository: TagRepository

    @BeforeEach
    fun setup() = tagRepository.deleteAll()

    @AfterEach
    fun teardown() = tagRepository.deleteAll()

    @Test
    fun getTagsShouldReturnOK(){
        tagRepository.save(Tag("some_tag"))
        tagRepository.save(Tag("some_other_tag"))
        given()
            .log().all()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .accept(MediaType.APPLICATION_JSON_VALUE)
            .port(port)
        .`when`()
            .get("/api/tags")
        .then()
            .log().all()
        .assertThat()
            .statusCode(HttpStatus.OK.value())
            .body("tags.size()", equalTo(2))
    }
}