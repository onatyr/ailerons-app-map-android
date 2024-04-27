package fr.onat68.aileronsappmapandroid.data.entities

import androidx.room.ColumnInfo
import androidx.room.Dao
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.PrimaryKey
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Entity(
    tableName = "individual"
)
data class Individual(
    @PrimaryKey val id: Int,
    @ColumnInfo(name = "name") val name: String,
    @ColumnInfo(name = "sex") val sex: String,
    @ColumnInfo(name = "pictures") val pictures: List<String>,
    @ColumnInfo(name = "common_name") val commonName: String,
    @ColumnInfo(name = "binomial_name") val binomialName: String,
    @ColumnInfo(name = "description") val description: String,
    @ColumnInfo(name = "icon") val icon: String,
    @ColumnInfo(name = "is_favorite") val isFavorite: Boolean = false
)

@Dao
interface IndividualDAO {
    @Query("SELECT * FROM individual")
    fun getAll(): Flow<List<Individual>>

    @Insert
    suspend fun insert(individual: Individual)

    @Query("DELETE FROM record_point")
    suspend fun deleteAll()

    @Query("SELECT * FROM individual WHERE is_favorite = 1")
    fun getListFavorite(): Flow<List<Individual>>

    @Query("UPDATE individual SET is_favorite = 1 WHERE id = :id")
    suspend fun addToFavorite(id: Int)

    @Query("UPDATE individual SET is_favorite = 0 WHERE id = :id")
    suspend fun removeFromFavorite(id: Int)
}
