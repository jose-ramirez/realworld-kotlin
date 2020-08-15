package dev.josers.realworld.service

import dev.josers.realworld.model.Article
import dev.josers.realworld.model.Profile
import dev.josers.realworld.model.User
import dev.josers.realworld.repository.ArticleRepository
import dev.josers.realworld.vo.request.ArticleRequestVO
import dev.josers.realworld.vo.request.ListArticleRequestVO
import dev.josers.realworld.vo.response.ArticleListResponseVO
import dev.josers.realworld.vo.response.ArticleResponseVO
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service


@Service
class ArticleService(@Autowired val articleRepository: ArticleRepository) {
    fun listArticles(params: ListArticleRequestVO) =
        ArticleListResponseVO(articles = articleRepository.findAll())

    // articles by people I follow, ordered by creation date
    fun feed(params: ListArticleRequestVO): ArticleListResponseVO {
        return ArticleListResponseVO(articles = articleRepository.findAll())
    }

    fun createArticle(request: ArticleRequestVO, loggedUser: User): ArticleResponseVO{
        val data = request.article

        val author = Profile(
            loggedUser.username,
            loggedUser.bio,
            loggedUser.image,
            false)

        val article = Article(
            slug = "",
            title = data?.title ?: "",
            description = data?.description ?: "",
            body = data?.body ?: "",
            author = author,
            favorited = false,
            favoritesCount = 0,
            tagList = emptyList())

        val savedArticle = articleRepository.save(article)

        return ArticleResponseVO(article = savedArticle)
    }

    fun updateArticle(slug: String, loggedUser: User){}

    fun deleteArticle(slug: String, loggedUser: User){}

    fun likeArticle(slug: String, loggedUser: User){}

    fun unlikeArticle(slug: String, loggedUser: User){}
}