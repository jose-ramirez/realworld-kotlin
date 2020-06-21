package dev.josers.realworld.service

import dev.josers.realworld.model.User
import dev.josers.realworld.repository.ArticleRepository
import dev.josers.realworld.vo.request.ListArticleRequestVO
import dev.josers.realworld.vo.response.ArticleListResponseVO
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.web.server.ResponseStatusException




@Service
class ArticleService(@Autowired val articleRepository: ArticleRepository) {
    fun listArticles(params: ListArticleRequestVO) =
        ArticleListResponseVO(articles = articleRepository.findAll())

    fun feed(){
        throw ResponseStatusException(HttpStatus.BAD_REQUEST, "Actor Not Found", Exception("some exception"))
    }

    fun createArticle(slug: String, loggedUser: User){}

    fun updateArticle(slug: String, loggedUser: User){}

    fun deleteArticle(slug: String, loggedUser: User){}

    fun likeArticle(slug: String, loggedUser: User){}

    fun unlikeArticle(slug: String, loggedUser: User){}
}