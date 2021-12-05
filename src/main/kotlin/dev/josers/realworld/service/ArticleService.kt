package dev.josers.realworld.service

import dev.josers.realworld.model.Article
import dev.josers.realworld.model.Like
import dev.josers.realworld.model.Profile
import dev.josers.realworld.model.User
import dev.josers.realworld.repository.ArticleRepository
import dev.josers.realworld.repository.LikeRepository
import dev.josers.realworld.utils.updateVal
import dev.josers.realworld.vo.request.ArticleRequestVO
import dev.josers.realworld.vo.request.ListArticleRequestVO
import dev.josers.realworld.vo.response.ArticleListResponseVO
import dev.josers.realworld.vo.response.ArticleResponseVO
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service


@Service
class ArticleService @Autowired constructor(
        val articleRepository: ArticleRepository,
        val likeRepository: LikeRepository) {

    companion object {
        const val NOT_IMPLEMENTED = "Not implemented yet"
    }

    // articles by params given, ordered by date
    fun list(params: ListArticleRequestVO) =
        ArticleListResponseVO(articles = articleRepository.findArticlesByParams(params))

    // articles by people I follow, ordered by creation date, paginated
    fun feed(loggedUser: User, params: ListArticleRequestVO): ArticleListResponseVO =
        ArticleListResponseVO(articles = articleRepository.findFeedByParams(loggedUser, params))

    fun create(request: ArticleRequestVO, loggedUser: User): ArticleResponseVO{
        val data = request.article

        val author = Profile(
            loggedUser.id!!,
            loggedUser.username,
            loggedUser.bio,
            loggedUser.image,
            false)

        val article = Article(
            id = null,
            slug = "",
            title = data?.title ?: "",
            description = data?.description ?: "",
            body = data?.body ?: "",
            author = author,
            favoritesCount = 0,
            tagList = emptyList())

        val savedArticle = articleRepository.save(article)

        return ArticleResponseVO(article = savedArticle)
    }

    fun update(slug: String, updatedArticleData: ArticleRequestVO, loggedUser: User){
        val oldArticle = articleRepository.findBySlug(slug)

        oldArticle?.title = oldArticle?.title?.updateVal(updatedArticleData.article?.title!!)!!
        oldArticle.description = oldArticle.description.updateVal(updatedArticleData.article?.description!!)
        oldArticle.body = oldArticle.body.updateVal(updatedArticleData.article.body!!)
        oldArticle.tagList = updatedArticleData.article.tagList!!

        articleRepository.save(oldArticle)
    }

    fun delete(slug: String, loggedUser: User){
        TODO(NOT_IMPLEMENTED)
    }

    fun like(slug: String, loggedUser: User){
        val article = articleRepository.findBySlug(slug)
        likeRepository.save(Like(articleId = article?.id!!, userId = loggedUser.id!!))
    }

    fun unlike(slug: String, loggedUser: User){
        val article = articleRepository.findBySlug(slug)
        val like = likeRepository.findByArticleId(article?.id!!)
        likeRepository.delete(like!!)
    }

    private fun userLikesArticle(user: User, slug: String): Boolean {
        val article = articleRepository.findBySlug(slug)
        val like = likeRepository.findByArticleId(article?.id!!)
        return user.id == like!!.userId
    }
}