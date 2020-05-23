package dev.josers.realworld.vo.request

class ListArticleRequestVO(
    var tag: String?,
    var author: String?,
    var favorited: Boolean?,
    limit: Int = 20,
    offset: Int = 0) {
}