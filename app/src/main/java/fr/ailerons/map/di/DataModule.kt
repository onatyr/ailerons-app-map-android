package fr.ailerons.map.di

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import fr.ailerons.map.data.AppDatabase
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataModule {

    @Provides
    @Singleton
    fun providesDatabase(@ApplicationContext context: Context) = Room.databaseBuilder(
        context,
        AppDatabase::class.java,
        "AileronsDatabase"
    ).build()

    @Provides
    @Singleton
    fun providesIndividualDao(database: AppDatabase) = database.individualDao()

    @Provides
    @Singleton
    fun providesArticleDao(database: AppDatabase) = database.articleDao()

    @Provides
    @Singleton
    fun providesRecordPointDao(database: AppDatabase) = database.recordPointDao()
}