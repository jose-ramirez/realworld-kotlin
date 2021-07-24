package dev.josers.realworld.vo.request

class ListArticleRequestVO(
    var tag: String?,
    var author: String?,
    var favorited: Boolean?,
    var limit: Int = 20,
    var offset: Int = 0)