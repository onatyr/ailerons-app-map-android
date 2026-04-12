package fr.ailerons.map.data.dtos

import fr.ailerons.map.DateUtil
import fr.ailerons.map.data.entities.Article
import kotlinx.serialization.Serializable

@Serializable
data class ArticleDto(
    val title: String,
    val content: String,
    val publicationDate: String,
    val imageUrl: String,
) {
    fun toArticle() = Article(
        id = 0,
        title = title,
        content = content,
        publicationDate = DateUtil.formatDate(publicationDate),
        imageUrl = imageUrl
    )
}