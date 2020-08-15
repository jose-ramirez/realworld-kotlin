package dev.josers.realworld.tests

import com.fasterxml.jackson.databind.ObjectMapper
import dev.josers.realworld.config.TestConfig
import dev.josers.realworld.model.Article
import dev.josers.realworld.repository.ArticleRepository
import dev.josers.realworld.repository.UserRepository
import dev.josers.realworld.utils.JWTUtils
import dev.josers.realworld.utils.TestUtils
import io.restassured.RestAssured.given
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.web.server.LocalServerPort
import org.springframework.context.annotation.Import
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType

@Import(TestConfig::class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ArticleModuleIntegrationTests: AbstractIntegrationTest() {

    @Autowired
    lateinit var mapper: ObjectMapper

    @Autowired
    lateinit var userRepository: UserRepository

    @Autowired
    lateinit var articleRepository: ArticleRepository

    @Autowired
    lateinit var jwtUtils: JWTUtils

    @Autowired
    lateinit var testUtils: TestUtils

    @BeforeEach
    fun setup() {
        articleRepository.deleteAll()
        userRepository.deleteAll()
    }

    @AfterEach
    fun tearDown() {
        articleRepository.deleteAll()
        userRepository.deleteAll()
    }

    @Test
    fun listArticlesShouldReturnOK(){
        given()
            .log().all()
            .port(port)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .accept(MediaType.APPLICATION_JSON_VALUE)
        .`when`()
            .get("/api/articles")
        .then()
            .log().all()
        .assertThat()
            .statusCode(HttpStatus.OK.value())
    }

    @Test
    fun createArticlesShouldCreateArticleAndReturnOK(){
        val user = testUtils.createFakeUser()
        userRepository.save(user)

        val token = jwtUtils.doGenerateToken(HashMap(), user.email)

        given()
            .log().all()
            .port(port)
            .header("Authorization", "Token $token")
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .accept(MediaType.APPLICATION_JSON_VALUE)
            .body(testUtils.createFakeArticle(user))
        .`when`()
            .post("/api/articles")
        .then()
            .log().all()
            .assertThat()
            .statusCode(HttpStatus.OK.value())

        val createdArticle = articleRepository.findAll()
        assert(createdArticle.isNotEmpty())
    }
}