package fr.ailerons.map.data.dtos

import fr.ailerons.map.DateUtil
import fr.ailerons.map.data.entities.Article
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ArticleDto(
    @SerialName("title")
    val title: String,

    @SerialName("content")
    val content: String,

    @SerialName("publication_date")
    val publicationDate: String,

    @SerialName("image_url")
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