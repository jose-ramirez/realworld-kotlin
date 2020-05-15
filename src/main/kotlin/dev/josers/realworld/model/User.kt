package dev.josers.realworld.model

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

@Document(collection = "users")
data class User(@Id var id: String? = null,
           var username: String,
           var email: String,
           var password: String,
           var bio: String?,
           var image: String?)