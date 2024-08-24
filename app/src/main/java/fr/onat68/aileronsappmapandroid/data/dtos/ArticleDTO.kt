package fr.onat68.aileronsappmapandroid.data.dtos

import fr.onat68.aileronsappmapandroid.data.entities.Article
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ArticleDTO(
    @SerialName("title")
    val title: String,

    @SerialName("content")
    val content: String,

    @SerialName("publication_date")
    val publicationDate: String,

    @SerialName("image_url")
    val imageUrl: String,
) {
    fun toArticleEntity() = Article(
        id = 0,
        title = title,
        content = content,
        publicationDate = publicationDate,
        imageUrl = imageUrl
    )
}