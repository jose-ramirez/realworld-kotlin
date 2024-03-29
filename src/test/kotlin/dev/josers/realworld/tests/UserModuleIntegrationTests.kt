package dev.josers.realworld.tests

import com.fasterxml.jackson.databind.ObjectMapper
import dev.josers.realworld.RestMethods.Users
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
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder


class UserModuleIntegrationTests: AbstractIntegrationTest() {

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
			.post(Users.V1.PATH)
		.then()
			.log().all()
		.assertThat()
			.statusCode(HttpStatus.OK.value())
			.body("user.username", equalTo("jane"))
			.body("user.email", equalTo("jane@doe.com"))
			.body("user.token", notNullValue())
	}

	@Test
	fun userRegistrationWithInvalidPasswordShouldReturnBadRequest() {

		val request = UserRequestVO(
			user = UserRequestVO.UserData(
				email = "jane@doe.com",
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
			.post(Users.V1.PATH)
		.then()
			.log().all()
		.assertThat()
			.statusCode(HttpStatus.BAD_REQUEST.value())
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
			.post(Users.V1.LOGIN)
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
			.get(Users.V1.USER)
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
			.put(Users.V1.PATH)
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
	fun updateUserWithMissingTokenShouldReturnForbidden() {
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
			.put(Users.V1.PATH)
		.then()
			.log().all()
		.assertThat()
			.statusCode(HttpStatus.FORBIDDEN.value())
	}

	@Test
	fun loginUserWithNonExiistingUserTokenShouldReturnNotFound() {
		val user = testUtils.createFakeUser()

		val token = jwtUtils.doGenerateToken(HashMap(), user.email)

		val request = UserLoginRequestVO(
			user = UserLoginRequestVO.Credentials(
				email = user.email,
				password = user.password
			)
		)

		given()
			.log().all()
			.port(port)
			.contentType(MediaType.APPLICATION_JSON_VALUE)
			.header("Authorization", "Token $token")
			.accept(MediaType.APPLICATION_JSON_VALUE)
			.body(mapper.writeValueAsString(request))
		.`when`()
			.post(Users.V1.LOGIN)
		.then()
			.log().all()
		.assertThat()
			.statusCode(HttpStatus.NOT_FOUND.value())
	}
}
