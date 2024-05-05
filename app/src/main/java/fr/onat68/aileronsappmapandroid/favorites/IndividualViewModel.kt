package fr.onat68.aileronsappmapandroid.favorites

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import fr.onat68.aileronsappmapandroid.data.entities.Individual
import fr.onat68.aileronsappmapandroid.data.repositories.IndividualRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class IndividualViewModel @Inject constructor(private val individualRepository: IndividualRepository) :
    ViewModel() {

    val individualsList: Flow<List<Individual>> = individualRepository.getListIndividual()
    val favoritesList: Flow<List<Individual>> = individualRepository.getListFavorite()

    private fun addFav(individualId: Int) =
        viewModelScope.launch { individualRepository.addToFavorite(individualId) }

    private fun deleteFav(individualId: Int) =
        viewModelScope.launch { individualRepository.removeFromFavorite(individualId) }

    fun changeFav(individual: Individual) {
        if (individual.isFavorite) {
            deleteFav(individual.id)
        } else {
            addFav(individual.id)
        }
    }
}