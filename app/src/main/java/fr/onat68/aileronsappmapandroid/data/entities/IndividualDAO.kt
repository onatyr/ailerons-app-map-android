package fr.onat68.aileronsappmapandroid.data.entities

import androidx.room.ColumnInfo
import androidx.room.Dao
import androidx.room.Entity
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
    )

@Dao
interface IndividualDAO {
    @Query("SELECT * FROM favorite")
    fun getAll(): List<Individual>
}
