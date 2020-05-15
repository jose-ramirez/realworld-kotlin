package dev.josers.realworld.repository

import dev.josers.realworld.model.User
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository

@Repository
interface UserRepository: MongoRepository<User, String> {

    fun findByUsername(username: String): User

    fun findByEmail(username: String): User?
}