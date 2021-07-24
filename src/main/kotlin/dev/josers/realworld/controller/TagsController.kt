package dev.josers.realworld.controller

import dev.josers.realworld.service.TagService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import dev.josers.realworld.RestMethods.Tags
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class TagsController(@Autowired val tagService: TagService) {
    @GetMapping(Tags.V1.PATH)
    fun getTags() = ResponseEntity.ok(tagService.getTags())
}