package fr.onat68.aileronsappmapandroid.favorites

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch

class FavoritesViewModel(private val database: AppDatabase) : ViewModel() {

    var favoritesList = database.FavoriteDao().getAll()

    private fun addFav(individualId: Int) {
        viewModelScope.launch {
            val newFavorite = Favorite(individualId)
            database.FavoriteDao().insert(newFavorite)
        }
    }

    private fun deleteFav(individualId: Int) {
        viewModelScope.launch {
            database.FavoriteDao().delete(individualId)
        }
    }


    fun changeFav(individualId: Int) {
        var idList = emptyList<Int>()
        viewModelScope.launch {
            favoritesList.collect { list ->
                idList = list.map { it.individualId }
            }
            if(idList.isNotEmpty()){
                cancel()
            }
        }

        if (idList.contains(individualId)) {
            deleteFav(individualId)
        } else {
            Log.d("Hey", "ICI")
            addFav(individualId)
        }
    }
}