package fr.onat68.aileronsappmapandroid.data.entities

import androidx.room.ColumnInfo
import androidx.room.Dao
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.PrimaryKey
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Entity(
    tableName = "article"
)
data class Article(
    @PrimaryKey(autoGenerate = true) val id: Int,
    @ColumnInfo(name = "title") val title: String,
    @ColumnInfo(name = "content") val content: String,
    @ColumnInfo(name = "publication_date") val publicationDate: String,
    @ColumnInfo(name = "image_url") val imageUrl: String,
)

@Dao
interface ArticleDAO {
    @Query("SELECT * FROM article")
    fun getAll(): Flow<List<Article>>

    @Insert
    suspend fun insertAll(articles: List<Article>)

    @Query("DELETE FROM article")
    suspend fun deleteAll()
}