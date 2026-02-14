package fr.ailerons.map.data

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import fr.ailerons.map.data.entities.Article
import fr.ailerons.map.data.entities.ArticleDAO
import fr.ailerons.map.data.entities.Individual
import fr.ailerons.map.data.entities.IndividualDAO
import fr.ailerons.map.data.entities.RecordPoint
import fr.ailerons.map.data.entities.RecordPointDAO

@Database(
    entities = [
        Individual::class,
        RecordPoint::class,
        Article::class
    ],
    version = 1,
    exportSchema = false
)
@TypeConverters(StringToStringListConverters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun articleDao(): ArticleDAO
    abstract fun individualDao(): IndividualDAO
    abstract fun recordPointDao(): RecordPointDAO
}