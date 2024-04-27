package fr.onat68.aileronsappmapandroid.favorites

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import fr.onat68.aileronsappmapandroid.data.entities.Favorite
import fr.onat68.aileronsappmapandroid.data.entities.Individual
import fr.onat68.aileronsappmapandroid.data.entities.IndividualDAO
import fr.onat68.aileronsappmapandroid.data.entities.IndividualDTO
import fr.onat68.aileronsappmapandroid.data.repositories.IndividualRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class FavoritesViewModel @Inject constructor(private val individualRepository: IndividualRepository) :
    ViewModel() {

    val favoritesList: Flow<List<Individual>> = individualRepository.getListFavorite()

    private fun addFav(individualId: Int) = individualRepository.addFavorite(individualId)

    private fun deleteFav(individualId: Int) = individualRepository.removeFavorite(individualId)

    fun changeFav(individual: Individual) {
        if (individual.isFavorite) {
            deleteFav(individual.id)
        } else {
            addFav(individual.id)
        }
    }
}