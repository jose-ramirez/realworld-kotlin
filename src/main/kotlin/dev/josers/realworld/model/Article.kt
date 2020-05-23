package dev.josers.realworld.model

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

@Document(collection = "articles")
class Article(@Id var id: String)