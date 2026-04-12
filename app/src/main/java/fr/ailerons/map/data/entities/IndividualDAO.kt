package fr.ailerons.map.data.entities

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
    @ColumnInfo(name = "individual_name") val
    individualName: String,
    val sex: String,
    @ColumnInfo(name = "common_name") val
    commonName: String,
    @ColumnInfo(name = "binomial_name") val
    binomialName: String,
    val description: String,
    val situation: String,
    val size: Double,
    val behavior: String,
    val color: String
)

@Dao
interface IndividualDAO {
    @Query("SELECT * FROM individual")
    fun getAll(): Flow<List<Individual>>

    @Query("SELECT id FROM individual")
    fun getAllIds(): Flow<List<Int>>

    @Query(
        """
        SELECT * FROM individual 
        WHERE EXISTS (
            SELECT 1 FROM record_point 
            WHERE record_point.id_individual = individual.id
        )
        """
    )
    fun getAllWithNonEmptyRecordPoints(): Flow<List<Individual>>

    @Query(
        """
        SELECT * FROM individual
            WHERE id = :id
    """
    )
    fun getById(id: Int): Flow<Individual?>

    @Query(
        """
            SELECT color from individual
        """
    )
    fun getColors(): Flow<List<String>>

    @Insert
    suspend fun insert(individual: Individual)

    @Query("DELETE FROM individual")
    suspend fun deleteAll()
}
