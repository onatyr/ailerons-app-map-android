package fr.onat68.aileronsappmapandroid.favorites

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class FavoritesViewModel(private val database: AppDatabase) : ViewModel() {

    val _favoritesList: MutableStateFlow<List<Favorite>> = MutableStateFlow(listOf())
    val favoritesList: Flow<List<Favorite>> = _favoritesList

    val initFav = getAll()
    private fun getAll() {
        viewModelScope.launch {
            _favoritesList.value = database.FavoriteDao().getAll()
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
                Log.d("Hey", "true")
                deleteFav(individualId)
            } else {
                Log.d("Hey", "false")
                addFav(individualId)
            }
        }
    }
}