package dev.josers.realworld.repository

import dev.josers.realworld.model.Tag
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository

@Repository
interface TagRepository: MongoRepository<Tag, String>{

}
