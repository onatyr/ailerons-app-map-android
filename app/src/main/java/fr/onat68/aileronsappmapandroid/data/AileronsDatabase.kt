package fr.onat68.aileronsappmapandroid.data

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import fr.onat68.aileronsappmapandroid.data.entities.Article
import fr.onat68.aileronsappmapandroid.data.entities.ArticleDAO
import fr.onat68.aileronsappmapandroid.data.entities.Individual
import fr.onat68.aileronsappmapandroid.data.entities.IndividualDAO
import fr.onat68.aileronsappmapandroid.data.entities.RecordPoint
import fr.onat68.aileronsappmapandroid.data.entities.RecordPointDAO

@Database(
    entities = [Individual::class, RecordPoint::class, Article::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(StringToStringListConverters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun articleDao(): ArticleDAO
    abstract fun individualDao(): IndividualDAO
    abstract fun recordPointDao(): RecordPointDAO
}