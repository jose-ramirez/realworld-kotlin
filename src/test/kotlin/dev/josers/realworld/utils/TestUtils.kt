package dev.josers.realworld.utils

import com.github.javafaker.Faker
import dev.josers.realworld.model.Article
import dev.josers.realworld.model.Profile
import dev.josers.realworld.model.User
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

@Component
class TestUtils(@Autowired val faker: Faker){

    fun createFakeUser() = User(
        email = faker.internet().emailAddress(),
        password = faker.internet().password(10, 12),
        username = faker.name().username(),
        bio = faker.lorem().sentence(4),
        image = faker.internet().image())

    fun createFakeArticle(loggedUser: User, tags: List<String> = emptyList()): Article {

        val author = Profile(
            loggedUser.username,
            loggedUser.bio,
            loggedUser.image,
            false
        )

        return Article(
            slug = "",
            title = faker.artist().name() ?: "",
            description = faker.lorem().sentence() ?: "",
            body = faker.lorem().paragraph() ?: "",
            author = author,
            favorited = false,
            favoritesCount = 0,
            tagList = tags
        )
    }
}