package dev.josers.realworld.model

import org.springframework.data.mongodb.core.mapping.Document

@Document(collection = "tags")
class Tag (val name: String)