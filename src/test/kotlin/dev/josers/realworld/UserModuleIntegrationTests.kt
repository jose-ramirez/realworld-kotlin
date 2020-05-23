package dev.josers.realworld

import com.fasterxml.jackson.databind.ObjectMapper
import dev.josers.realworld.config.TestConfig
import dev.josers.realworld.repository.UserRepository
import dev.josers.realworld.utils.JWTUtils
import dev.josers.realworld.utils.TestUtils
import dev.josers.realworld.vo.request.UserLoginRequestVO
import dev.josers.realworld.vo.request.UserRequestVO
import io.restassured.RestAssured.given
import org.hamcrest.Matchers.equalTo
import org.hamcrest.Matchers.notNullValue
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.web.server.LocalServerPort
import org.springframework.context.annotation.Import
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder

@Import(TestConfig::class)
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

	@Autowired
	lateinit var testUtils: TestUtils

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

		val user = testUtils.createFakeUser()
		val rawPassword = user.password.substring(0)
		user.password = encoder.encode(user.password)
		userRepository.save(user)

		val request = UserLoginRequestVO(
			user = UserLoginRequestVO.Credentials(
				email = user.email,
				password = rawPassword
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
			.body("user.username", equalTo(user.username))
			.body("user.email", equalTo(user.email))
			.body("user.token", notNullValue())
	}

	@Test
	fun getUserWithValidTokenShouldReturnOK() {

		val user = testUtils.createFakeUser()
		userRepository.save(user)

		val token = jwtUtils.doGenerateToken(HashMap(), user.email)

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
			.body("user.username", equalTo(user.username))
			.body("user.email", equalTo(user.email))
			.body("user.token", notNullValue())
	}

	@Test
	fun updateUserWithValidTokenShouldReturnOK() {

		val user = testUtils.createFakeUser()
		userRepository.save(user)

		val token = jwtUtils.doGenerateToken(HashMap(), user.email)

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

		val updatedUser = userRepository.findByEmail(user.email)
		assert(updatedUser?.username.equals("marie"))
	}

	@Test
	fun updateUserWithMissingTokenShouldReturnUnauthorized() {
		val request = UserRequestVO(
			user = UserRequestVO.UserData(
				username = "marie"
			)
		)

		given()
			.log().all()
			.port(port)
			.contentType(MediaType.APPLICATION_JSON_VALUE)
			.accept(MediaType.APPLICATION_JSON_VALUE)
			.body(mapper.writeValueAsString(request))
		.`when`()
			.put("/api/users")
		.then()
			.log().all()
		.assertThat()
			.statusCode(HttpStatus.UNAUTHORIZED.value())
	}
}
