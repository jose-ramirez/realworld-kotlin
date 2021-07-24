package dev.josers.realworld.controller

import dev.josers.realworld.RestMethods.Articles
import dev.josers.realworld.model.User
import dev.josers.realworld.service.ArticleService
import dev.josers.realworld.vo.request.ArticleRequestVO
import dev.josers.realworld.vo.request.ListArticleRequestVO
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
class ArticlesController(@Autowired val articleService: ArticleService) {

    @GetMapping(Articles.V1.PATH)
    fun list(params: ListArticleRequestVO) =
        ResponseEntity.ok(articleService.list(params))

    @GetMapping(Articles.V1.FEED)
    fun feed(params: ListArticleRequestVO) =
        ResponseEntity.ok(articleService.feed(params))

    @PostMapping(Articles.V1.PATH)
    fun create(request: ArticleRequestVO, loggedUser: User) =
        ResponseEntity.ok(articleService.create(request, loggedUser))

    @PutMapping(Articles.V1.ARTICLES_BY_SLUG)
    fun update(
            @PathVariable("slug") slug: String,
            @RequestBody request: ArticleRequestVO,
            loggedUser: User) =
        ResponseEntity.ok(articleService.update(slug, request, loggedUser))
}