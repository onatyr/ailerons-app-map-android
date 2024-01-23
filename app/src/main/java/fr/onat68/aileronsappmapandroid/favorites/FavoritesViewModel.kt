package fr.onat68.aileronsappmapandroid.favorites

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import fr.onat68.aileronsappmapandroid.species.Individual
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class FavoritesViewModel(
    private val database: AppDatabase,
    private val individualsList: List<Individual>
) : ViewModel() {


    private val _favoritesList: MutableStateFlow<List<Individual>> = MutableStateFlow(listOf())
    val favoritesList: Flow<List<Individual>> = _favoritesList

        val initFav = getAll()
    private fun getAll() {

        viewModelScope.launch {
            _favoritesList.value = individualsList.filter {
                database.FavoriteDao().getAll().map { fav -> fav.individualId }
                    .contains(it.individualRecordId)
            }
        }
    }

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