package fr.onat68.aileronsappmapandroid.data.entities

import androidx.room.ColumnInfo
import androidx.room.Dao
import androidx.room.Entity
import androidx.room.Index
import androidx.room.Insert
import androidx.room.PrimaryKey
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Entity(
    tableName = "favorite",
    indices = [Index("individualId")]
)
data class Favorite(
    @ColumnInfo(name = "individualId") val individualId: Int
) {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    var favoriteId: Long = 0
}

@Dao
interface FavoriteDAO {
    @Query("SELECT * FROM favorite")
    fun getAll(): Flow<List<Favorite>>

    @Query("DELETE FROM favorite WHERE individualId = :id")
    suspend fun delete(id: Int)
}
