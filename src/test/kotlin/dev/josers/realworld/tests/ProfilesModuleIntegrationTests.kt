package dev.josers.realworld.tests

import dev.josers.realworld.RestMethods.Profiles
import dev.josers.realworld.model.Following
import dev.josers.realworld.repository.FollowingRepository
import dev.josers.realworld.repository.UserRepository
import dev.josers.realworld.utils.JWTUtils
import dev.josers.realworld.utils.TestUtils
import io.restassured.RestAssured.given
import org.hamcrest.CoreMatchers.equalTo
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.util.CollectionUtils

class ProfilesModuleIntegrationTests: AbstractIntegrationTest() {

    @Autowired
    lateinit var followingRepository: FollowingRepository

    @Autowired
    lateinit var userRepository: UserRepository

    @Autowired
    lateinit var testUtils: TestUtils

    @Autowired
    lateinit var jwtUtils: JWTUtils

    @BeforeEach
    fun setup() {
        followingRepository.deleteAll()
        userRepository.deleteAll()
    }

    @AfterEach
    fun teardown() {
        followingRepository.deleteAll()
        userRepository.deleteAll()
    }

    @Test
    fun getProfileShouldReturnOK() {
        val user = testUtils.createFakeUser()
        userRepository.save(user)

        given()
            .log().all()
            .port(port)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .accept(MediaType.APPLICATION_JSON_VALUE)
            .pathParam("username", user.username)
            .`when`()
        .get(Profiles.V1.PROFILE)
            .then()
        .log().all()
            .assertThat()
            .statusCode(HttpStatus.OK.value())
            .body("profile.username", equalTo(user.username))
            .body("profile.bio", equalTo(user.bio))
            .body("profile.image", equalTo(user.image))
    }

    @Test
    fun followingUserWithValidTokenShouldReturnOK() {
        val followed = testUtils.createFakeUser()
        val follower = testUtils.createFakeUser()
        userRepository.save(followed)
        userRepository.save(follower)
        val followerToken = jwtUtils.doGenerateToken(HashMap(), follower.email)

        given()
            .log().all()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .accept(MediaType.APPLICATION_JSON_VALUE)
            .port(port)
            .header("Authorization", "Token $followerToken")
            .pathParam("username", followed.username)
        .`when`()
            .post(Profiles.V1.PROFILE_FOLLOW)
        .then()
            .log().all()
        .assertThat()
            .statusCode(HttpStatus.OK.value())
            .body("profile.username", equalTo(followed.username))
            .body("profile.bio", equalTo(followed.bio))
            .body("profile.image", equalTo(followed.image))

        val following = followingRepository.findAll()
        assert(following[0].idFollowed != null)
    }

    @Test
    fun unfollowingUserWithValidTokenShouldReturnOK() {
        val followed = testUtils.createFakeUser()
        val follower = testUtils.createFakeUser()
        userRepository.save(followed)
        userRepository.save(follower)
        val followerToken = jwtUtils.doGenerateToken(HashMap(), follower.email)

        followingRepository.save(Following(
            idFollowed = followed.id,
            idFollower = follower.id
        ))

        given()
            .log().all()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .accept(MediaType.APPLICATION_JSON_VALUE)
            .port(port)
            .header("Authorization", "Token $followerToken")
            .pathParam("username", followed.username)
        .`when`()
            .delete(Profiles.V1.PROFILE_FOLLOW)
        .then()
            .log().all()
        .assertThat()
            .statusCode(HttpStatus.OK.value())

        val following = followingRepository.findAll()
        assert(CollectionUtils.isEmpty(following))
    }
}