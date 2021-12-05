package dev.josers.realworld.repository

import dev.josers.realworld.model.Like
import org.springframework.data.mongodb.repository.MongoRepository

interface LikeRepository: MongoRepository<Like, String> {
    fun findByArticleId(articleId: String): Like?
}