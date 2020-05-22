package dev.josers.realworld.model

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

@Document(collection = "followings")
data class Following (@Id var id: String? = null,
                      val idFollower: String? = "",
                      val idFollowed: String? = "")