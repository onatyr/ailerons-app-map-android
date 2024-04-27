package fr.onat68.aileronsappmapandroid.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import fr.onat68.aileronsappmapandroid.data.entities.Individual
import fr.onat68.aileronsappmapandroid.data.entities.IndividualDAO
import fr.onat68.aileronsappmapandroid.data.entities.RecordPoint
import fr.onat68.aileronsappmapandroid.data.entities.RecordPointDAO

@Database(entities = [Individual::class, RecordPoint::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun individualDao(): IndividualDAO
    abstract fun recordPointDao(): RecordPointDAO

    companion object {

        @Volatile
        private var instance: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return instance ?: synchronized(this) {
                instance ?: buildDatabase(context).also { instance = it }
            }
        }

        private fun buildDatabase(context: Context): AppDatabase {
            return Room.databaseBuilder(context, AppDatabase::class.java, "AileronsDatabase")
                .build()
        }
    }
}