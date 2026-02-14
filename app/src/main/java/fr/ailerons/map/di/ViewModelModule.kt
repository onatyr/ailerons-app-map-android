package fr.ailerons.map.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import fr.ailerons.map.data.repositories.ArticleRepository
import fr.ailerons.map.data.repositories.IndividualRepository
import fr.ailerons.map.data.repositories.RecordPointRepository
import fr.ailerons.map.presentation.screens.individual.IndividualViewModel
import fr.ailerons.map.presentation.screens.map.MapViewModel
import fr.ailerons.map.presentation.screens.news.NewsViewModel

@Module
@InstallIn(ViewModelComponent::class)
object ViewModelModule {

    @Provides
    fun provideNewsViewModel(articleRepository: ArticleRepository) =
        NewsViewModel(articleRepository)

    @Provides
    fun provideIndividualViewModel(individualRepository: IndividualRepository) =
        IndividualViewModel(individualRepository)

    @Provides
    fun provideMapViewModel(
        recordPointRepository: RecordPointRepository,
    ): MapViewModel {
        return MapViewModel(recordPointRepository)
    }
}