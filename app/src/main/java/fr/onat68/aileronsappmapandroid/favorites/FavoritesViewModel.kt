package fr.onat68.aileronsappmapandroid.favorites

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class FavoritesViewModel(private val database: AppDatabase) : ViewModel() {

    var favoritesList = database.FavoriteDao().getAll()

    fun addFav(individualId: Int) {
        viewModelScope.launch {
            val newFavorite = Favorite(individualId)
            database.FavoriteDao().insert(newFavorite)
        }
    }

    fun deleteFav(favorite: Favorite) {
        viewModelScope.launch {
            database.FavoriteDao().delete(favorite)
        }
    }
}