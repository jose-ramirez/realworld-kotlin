package dev.josers.realworld.model

import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.Id
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.mongodb.core.mapping.Document
import java.util.*

@Document(collection = "likes")
class Like (
    @Id var id: String? = null,
    var articleId: String,
    var userId: String,
    @CreatedDate var createdAt: Date = Date(),
    @LastModifiedDate var updatedAt: Date = Date()
)