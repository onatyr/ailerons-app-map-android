package fr.onat68.aileronsappmapandroid.favorites

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState

@Composable
fun FavoriteScreen(favoritesViewModel: FavoritesViewModel){

    val favoritesList: State<List<Favorite>> = favoritesViewModel.favoritesList.collectAsState(initial = listOf())

    Column {
        for (fav in favoritesList.value) {
            Text(text = fav.individualId.toString())
        }
    }
}