package fr.onat68.aileronsappmapandroid.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
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
    fun provideMapViewModel(recordPointRepository: RecordPointRepository): MapViewModel {
        return MapViewModel(recordPointRepository)
    }
}