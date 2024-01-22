package fr.onat68.aileronsappmapandroid.favorites

import android.content.Context
import androidx.compose.runtime.MutableState
import androidx.room.ColumnInfo
import androidx.room.Dao
import androidx.room.Database
import androidx.room.Delete
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.PrimaryKey
import androidx.room.Query
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.Index
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

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
interface FavoriteDao {
    @Query("SELECT * FROM favorite")
    fun getAll(): Flow<List<Favorite>>

    @Insert
    suspend fun insert(favorite: Favorite)

    @Query("DELETE FROM favorite WHERE individualId = :id")
    suspend fun delete(id: Int)
}

@Database(entities = [Favorite::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun FavoriteDao(): FavoriteDao

    companion object {

        // For Singleton instantiation
        @Volatile
        private var instance: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return instance ?: synchronized(this) {
                instance ?: buildDatabase(context).also { instance = it }
            }
        }

        private fun buildDatabase(context: Context): AppDatabase {
            return Room.databaseBuilder(context, AppDatabase::class.java, "favorites").build()
        }
    }
}
