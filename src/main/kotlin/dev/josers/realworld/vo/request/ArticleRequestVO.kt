package dev.josers.realworld.vo.request

data class ArticleRequestVO(val article: ArticleData? = null) {
    data class ArticleData(
        var title: String? = "",
        var description: String? = "",
        var body: String? = "",
        var tagList: List<String>? = emptyList())
}