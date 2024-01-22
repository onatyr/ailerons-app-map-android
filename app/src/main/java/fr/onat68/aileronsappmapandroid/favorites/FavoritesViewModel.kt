package fr.onat68.aileronsappmapandroid.favorites

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class FavoritesViewModel(private val database: AppDatabase): ViewModel() {

    var favoritesList = database.FavoriteDao().getAll()

    fun addFav(id: Int){
        viewModelScope.launch {
            val newFavorite = Favorite(id)
            database.FavoriteDao().insert(newFavorite)
            favoritesList = database.FavoriteDao().getAll()
        }
    }
}