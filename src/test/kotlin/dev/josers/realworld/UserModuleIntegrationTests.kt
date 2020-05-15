package dev.josers.realworld

import com.fasterxml.jackson.databind.ObjectMapper
import dev.josers.realworld.config.JWTUtils
import dev.josers.realworld.model.User
import dev.josers.realworld.repository.UserRepository
import dev.josers.realworld.request.UserLoginRequestVO
import dev.josers.realworld.request.UserRequestVO
import io.restassured.RestAssured.given
import org.hamcrest.Matchers.equalTo
import org.hamcrest.Matchers.notNullValue
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.web.server.LocalServerPort
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class UserModuleIntegrationTests {

	@LocalServerPort
	var port: Int = 0

	@Autowired
	lateinit var mapper: ObjectMapper

	@Autowired
	lateinit var userRepository: UserRepository

	@Autowired
	lateinit var encoder: BCryptPasswordEncoder

	@Autowired
	lateinit var jwtUtils: JWTUtils

	@BeforeEach
	fun setup() {
		userRepository.deleteAll()
	}

	@AfterEach
	fun tearDown() {
		userRepository.deleteAll()
	}

	@Test
	fun userRegisteredShouldReturnOK() {

		val request = UserRequestVO(
			user = UserRequestVO.UserData(
				email = "jane@doe.com",
				password = "secret",
				image = null,
				bio = null,
				username = "jane"
			)
		)

		given()
			.log().all()
			.port(port)
			.contentType(MediaType.APPLICATION_JSON_VALUE)
			.accept(MediaType.APPLICATION_JSON_VALUE)
			.body(mapper.writeValueAsString(request))
		.`when`()
			.post("/api/users")
		.then()
			.log().all()
		.assertThat()
			.statusCode(HttpStatus.OK.value())
			.body("user.username", equalTo("jane"))
			.body("user.email", equalTo("jane@doe.com"))
			.body("user.token", notNullValue())
	}

	@Test
	fun loginWithValidCredentialsShouldReturnOK() {

		userRepository.save(User(
			email = "jane@doe.com",
			password = encoder.encode("secret"),
			image = null,
			bio = null,
			username = "jane"))

		val request = UserLoginRequestVO(
			user = UserLoginRequestVO.Credentials(
				email = "jane@doe.com",
				password = "secret"
			)
		)

		given()
			.log().all()
			.port(port)
			.contentType(MediaType.APPLICATION_JSON_VALUE)
			.accept(MediaType.APPLICATION_JSON_VALUE)
			.body(mapper.writeValueAsString(request))
		.`when`()
			.post("/api/users/login")
		.then()
			.log().all()
		.assertThat()
			.statusCode(HttpStatus.OK.value())
			.body("user.username", equalTo("jane"))
			.body("user.email", equalTo("jane@doe.com"))
			.body("user.token", notNullValue())
	}

	@Test
	fun getUserWithValidTokenShouldReturnOK() {

		userRepository.save(User(
				email = "jane@doe.com",
				password = encoder.encode("secret"),
				image = null,
				bio = null,
				username = "jane"))

		val token = jwtUtils.doGenerateToken(HashMap(), "jane@doe.com")

		given()
			.log().all()
			.port(port)
			.header("Authorization", "Token $token")
			.contentType(MediaType.APPLICATION_JSON_VALUE)
			.accept(MediaType.APPLICATION_JSON_VALUE)
		.`when`()
			.get("/api/user")
		.then()
			.log().all()
		.assertThat()
			.statusCode(HttpStatus.OK.value())
			.body("user.username", equalTo("jane"))
			.body("user.email", equalTo("jane@doe.com"))
			.body("user.token", notNullValue())
	}

	@Test
	fun updateUserWithValidTokenShouldReturnOK() {

		userRepository.save(User(
			email = "jane@doe.com",
			password = "secret",
			image = null,
			bio = null,
			username = "jane"))

		val token = jwtUtils.doGenerateToken(HashMap(), "jane@doe.com")

		val request = UserRequestVO(
			user = UserRequestVO.UserData(
				username = "marie"
			)
		)

		given()
			.log().all()
			.port(port)
			.header("Authorization", "Token $token")
			.contentType(MediaType.APPLICATION_JSON_VALUE)
			.accept(MediaType.APPLICATION_JSON_VALUE)
			.body(mapper.writeValueAsString(request))
		.`when`()
			.put("/api/users")
		.then()
			.log().all()
		.assertThat()
			.statusCode(HttpStatus.OK.value())
			.body("user.username", equalTo("marie"))
			.body("user.token", notNullValue())

		val updatedUser = userRepository.findByEmail("jane@doe.com")
		assert(updatedUser?.username.equals("marie"))
	}
}
