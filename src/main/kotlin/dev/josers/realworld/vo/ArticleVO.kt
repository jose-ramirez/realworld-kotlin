package dev.josers.realworld.vo

import dev.josers.realworld.model.Profile
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.Id
import org.springframework.data.annotation.LastModifiedDate
import java.util.*

class ArticleVO (
    @Id var id: String? = null,
    var slug: String,
    var title: String = "",
    var description: String,
    var body: String,
    var tagList: List<String>,
    var favorited: Boolean,
    var favoritesCount: Int,
    @CreatedDate var createdAt: Date = Date(),
    @LastModifiedDate var updatedAt: Date = Date(),
    var author: Profile
)

//{
//    "article": {
//    "slug": "how-to-train-your-dragon",
//    "title": "How to train your dragon",
//    "description": "Ever wonder how?",
//    "body": "It takes a Jacobian",
//    "tagList": ["dragons", "training"],
//    "createdAt": "2016-02-18T03:22:56.637Z",
//    "updatedAt": "2016-02-18T03:48:35.824Z",
//    "favorited": false,
//    "favoritesCount": 0,
//    "author": {
//        "username": "jake",
//        "bio": "I work at statefarm",
//        "image": "https://i.stack.imgur.com/xHWG8.jpg",
//        "following": false
//    }
//}
//}