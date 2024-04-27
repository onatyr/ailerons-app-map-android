package fr.onat68.aileronsappmapandroid.data

import androidx.room.Database
import androidx.room.RoomDatabase
import fr.onat68.aileronsappmapandroid.data.entities.Favorite
import fr.onat68.aileronsappmapandroid.data.entities.FavoriteDAO
import fr.onat68.aileronsappmapandroid.data.entities.Individual
import fr.onat68.aileronsappmapandroid.data.entities.IndividualDAO

@Database(entities = [Favorite::class, Individual::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun favoriteDao(): FavoriteDAO
    abstract fun individualDao(): IndividualDAO
}