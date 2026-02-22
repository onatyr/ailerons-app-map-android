package fr.ailerons.map.di

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import fr.ailerons.map.BuildConfig
import fr.ailerons.map.data.AppDatabase
import fr.ailerons.map.data.repositories.ArticleRepository
import fr.ailerons.map.data.repositories.IndividualRepository
import fr.ailerons.map.data.repositories.RecordPointRepository
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.postgrest.Postgrest
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context) = Room.databaseBuilder(
        context,
        AppDatabase::class.java,
        "AileronsDatabase"
    ).build()

    @Provides
    @Singleton
    fun provideSupabaseClient() =
        createSupabaseClient(BuildConfig.supabaseUrl, BuildConfig.supabaseKey) {
            install(Postgrest)
        }

    @Provides
    fun provideArticleRepository(supabaseClient: SupabaseClient, database: AppDatabase) =
        ArticleRepository(supabaseClient, database.articleDao())

    @Provides
    fun provideIndividualRepository(
        supabaseClient: SupabaseClient,
        database: AppDatabase
    ) = IndividualRepository(supabaseClient, database.individualDao())

    @Provides
    fun provideRecordPointRepository(
        supabaseClient: SupabaseClient,
        database: AppDatabase
    ) = RecordPointRepository(supabaseClient, database.recordPointDao())
}