package fr.onat68.aileronsappmapandroid.favorites

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import fr.onat68.aileronsappmapandroid.data.entities.Favorite
import fr.onat68.aileronsappmapandroid.data.entities.IndividualDTO
import fr.onat68.aileronsappmapandroid.data.repositories.IndividualRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class FavoritesViewModel @Inject constructor(private val individualRepository: IndividualRepository)
: ViewModel() {

    val favoritesList: Flow<List<Favorite>> = individualRepository.getListFavorite()


    private fun addFav(individualId: Int) {

        viewModelScope.launch {
            val newFavorite = Favorite(individualId)
            database.FavoriteDao().insert(newFavorite)
            getAll()
        }
    }

    private fun deleteFav(individualId: Int) {

        viewModelScope.launch {
            database.FavoriteDao().delete(individualId)
            getAll()
        }
    }

    fun changeFav(individualId: Int) {

        viewModelScope.launch {
            if (database.FavoriteDao().getAll().map { it.individualId }.contains(individualId)) {
                deleteFav(individualId)
            } else {
                addFav(individualId)
            }
        }
    }
}