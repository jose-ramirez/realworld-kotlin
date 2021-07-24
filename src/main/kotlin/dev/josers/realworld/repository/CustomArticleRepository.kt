package dev.josers.realworld.repository

import dev.josers.realworld.model.Article
import dev.josers.realworld.vo.request.ListArticleRequestVO

interface CustomArticleRepository {
    fun findArticlesByParams(request: ListArticleRequestVO): List<Article>

    fun findArticleFeedByParams(request: ListArticleRequestVO): List<Article>
}