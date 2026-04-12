package fr.ailerons.map.data.entities

import androidx.room.ColumnInfo
import androidx.room.Dao
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Insert
import androidx.room.PrimaryKey
import androidx.room.Query
import androidx.room.Relation
import kotlinx.coroutines.flow.Flow

data class RecordPointWithColor(
    @Embedded val recordPoint: RecordPoint,
    @Relation(
        parentColumn = "id_individual",
        entityColumn = "id",
        entity = Individual::class,
        projection = ["color"]
    )
    val color: String
)

@Entity(
    tableName = "record_point",
    foreignKeys = [
        ForeignKey(
            entity = Individual::class,
            parentColumns = ["id"],
            childColumns = ["id_individual"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class RecordPoint(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val longitude: Float,
    val latitude: Float,
    @ColumnInfo(name = "id_individual", index = true)
    val idIndividual: Int,
    @ColumnInfo(name = "record_timestamp")
    val recordTimestamp: String,
    val depth: Int?
)

@Dao
interface RecordPointDAO {
    @Query(
        """
        SELECT * FROM record_point
            WHERE id_individual IN (:ids)
        """
    )
    fun getWithColorByIds(ids: List<Int>): Flow<List<RecordPointWithColor>>

    @Insert
    suspend fun insert(recordPoint: RecordPoint)

    @Insert
    suspend fun insertAll(recordPoints: List<RecordPoint>)

    @Query("DELETE FROM record_point")
    suspend fun deleteAll()
}
