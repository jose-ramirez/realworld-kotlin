package dev.josers.realworld

import com.github.javafaker.Faker
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
}