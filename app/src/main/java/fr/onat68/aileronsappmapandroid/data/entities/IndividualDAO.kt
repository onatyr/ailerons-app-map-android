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
    @ColumnInfo(name = "individual_name") val individualName: String,
    @ColumnInfo(name = "sex") val sex: String,
    @ColumnInfo(name = "common_name") val commonName: String,
    @ColumnInfo(name = "binomial_name") val binomialName: String,
    @ColumnInfo(name = "description") val description: String,
    @ColumnInfo(name = "situation") val situation: String,
    @ColumnInfo("size") val size: Int,
    @ColumnInfo("behavior") val behavior: String,
)

@Dao
interface IndividualDAO {
    @Query("SELECT * FROM individual")
    fun getAll(): Flow<List<Individual>>

    @Insert
    suspend fun insert(individual: Individual)

    @Query("DELETE FROM individual")
    suspend fun deleteAll()
}
