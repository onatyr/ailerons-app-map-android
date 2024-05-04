package fr.onat68.aileronsappmapandroid.data

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import fr.onat68.aileronsappmapandroid.data.entities.Individual
import fr.onat68.aileronsappmapandroid.data.entities.IndividualDAO
import fr.onat68.aileronsappmapandroid.data.entities.RecordPoint
import fr.onat68.aileronsappmapandroid.data.entities.RecordPointDAO

@Database(entities = [Individual::class, RecordPoint::class], version = 1)
@TypeConverters(StringToStringListConverters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun individualDao(): IndividualDAO
    abstract fun recordPointDao(): RecordPointDAO
}