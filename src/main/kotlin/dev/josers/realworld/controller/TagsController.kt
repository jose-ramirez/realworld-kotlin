package dev.josers.realworld.controller

import dev.josers.realworld.service.TagService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class TagsController(@Autowired val tagService: TagService) {
    @GetMapping("/api/tags")
    fun getTags() = ResponseEntity.ok(tagService.getTags())
}