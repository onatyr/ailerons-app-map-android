package fr.onat68.aileronsappmapandroid.data.entities

import androidx.room.ColumnInfo
import androidx.room.Dao
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.PrimaryKey
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Entity(
    tableName = "record_point"
)
data class RecordPoint(
    @PrimaryKey val id: Int,
    @ColumnInfo(name = "longitude") val longitude: Float,
    @ColumnInfo(name = "latitude") val latitude: Float,
    @ColumnInfo(name = "individual_id") val individualId: Int?,
    @ColumnInfo(name = "record_timestamp") val recordTimestamp: String,
    @ColumnInfo(name = "depth") val depth: Int?
)

@Dao
interface RecordPointDAO {
    @Query("SELECT * FROM record_point")
    fun getAll(): Flow<List<RecordPoint>>

    @Insert
    suspend fun insert(recordPoint: RecordPoint)

    @Query("DELETE FROM record_point")
    suspend fun deleteAll()
}
