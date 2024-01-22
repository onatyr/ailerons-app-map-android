package fr.onat68.aileronsappmapandroid.favorites

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState

@Composable
fun FavoriteScreen(favoritesViewModel: FavoritesViewModel) {

    val favoritesList: State<List<Favorite>> =
        favoritesViewModel.favoritesList.collectAsState(initial = listOf())

    Column {
        Button(onClick = { favoritesViewModel.addFav(5) }) {

        }
        for (fav in favoritesList.value) {
            Row {
                Text(text = fav.individualId.toString())
                Button(onClick = { favoritesViewModel.deleteFav(fav) }) {

                }
            }
        }
    }
}