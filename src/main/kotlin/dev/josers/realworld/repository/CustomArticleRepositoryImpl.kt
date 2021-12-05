package dev.josers.realworld.repository

import dev.josers.realworld.model.Article
import dev.josers.realworld.model.User
import dev.josers.realworld.vo.request.ListArticleRequestVO
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.data.mongodb.core.query.Query
import org.springframework.data.mongodb.core.query.Criteria.where

class CustomArticleRepositoryImpl constructor(
        private val mongoTemplate: MongoTemplate,
        private val followingRepository: FollowingRepository) : CustomArticleRepository {

    override fun findArticlesByParams(request: ListArticleRequestVO): List<Article> {
        // limit can't be zero!
        var query = Query()
            .with(Sort.by(Sort.Direction.DESC, "createdAt"))
            .with(PageRequest.of(request.offset / request.limit, request.limit))

        if (request.author != null) {
            query = query.addCriteria(where("author.username").regex(request.author!!))
        }
        if (request.tag != null) {
            query = query.addCriteria(where("tagList").`is`(request.tag))
        }

        return mongoTemplate.find(query, Article::class.java)
    }

    override fun findFeedByParams(loggedUser: User, request: ListArticleRequestVO): List<Article> {
        val followedUserIds = followingRepository.findByIdFollower(loggedUser.id!!).map { it.idFollowed }

        val query = Query()
            .with(Sort.by(Sort.Direction.DESC, "createdAt"))
            .with(PageRequest.of(request.offset / request.limit, request.limit))
            .addCriteria(where("author.idUser").`in`(followedUserIds))

        return mongoTemplate.find(query, Article::class.java)
    }
}