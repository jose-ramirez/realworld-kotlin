package dev.josers.realworld.controller

import dev.josers.realworld.model.User
import dev.josers.realworld.service.ArticleService
import dev.josers.realworld.vo.request.ArticleRequestVO
import dev.josers.realworld.vo.request.ListArticleRequestVO
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class ArticlesController(@Autowired val articleService: ArticleService) {
    @GetMapping("/api/articles")
    fun listArticles(params: ListArticleRequestVO) =
        ResponseEntity.ok(articleService.listArticles(params))

    @GetMapping("/api/articles/feed")
    fun feed(params: ListArticleRequestVO) =
        ResponseEntity.ok(articleService.feed(params))

    @PostMapping("/api/articles")
    fun createArticle(request: ArticleRequestVO, loggedUser: User) =
        ResponseEntity.ok(articleService.createArticle(request, loggedUser))
}