package fr.onat68.aileronsappmapandroid.di

import android.content.Context
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.graphics.drawable.toBitmap
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import fr.onat68.aileronsappmapandroid.R
import fr.onat68.aileronsappmapandroid.data.repositories.IndividualRepository
import fr.onat68.aileronsappmapandroid.data.repositories.RecordPointRepository
import fr.onat68.aileronsappmapandroid.favorites.IndividualViewModel
import fr.onat68.aileronsappmapandroid.map.MapViewModel

@Module
@InstallIn(ViewModelComponent::class)
object ViewModelModule {

    @Provides
    fun provideIndividualViewModel(individualRepository: IndividualRepository): IndividualViewModel {
        return IndividualViewModel(individualRepository)
    }

    @Provides
    fun provideMapViewModel(
        recordPointRepository: RecordPointRepository,
        @ApplicationContext context: Context
    ): MapViewModel {
        val bitmapMarker = AppCompatResources.getDrawable(context, R.drawable.red_marker)!!.toBitmap()
        return MapViewModel(recordPointRepository, bitmapMarker)
    }
}