package dev.josers.realworld.repository

import dev.josers.realworld.model.Article
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository

@Repository
interface ArticleRepository: MongoRepository<Article, String> {
}