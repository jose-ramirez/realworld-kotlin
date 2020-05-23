package dev.josers.realworld.service

import dev.josers.realworld.repository.ArticleRepository
import dev.josers.realworld.vo.request.ListArticleRequestVO
import dev.josers.realworld.vo.response.ArticleListResponseVO
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class ArticleService(@Autowired val articleRepository: ArticleRepository) {
    fun listArticles(params: ListArticleRequestVO) =
        ArticleListResponseVO(articles = articleRepository.findAll())
}