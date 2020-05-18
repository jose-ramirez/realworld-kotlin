package dev.josers.realworld.service

import dev.josers.realworld.repository.TagRepository
import dev.josers.realworld.response.TagResponseVO
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class TagService(@Autowired val tagRepository: TagRepository) {
    fun getTags() = TagResponseVO(tagRepository.findAll().map { tag -> tag.name })

}
