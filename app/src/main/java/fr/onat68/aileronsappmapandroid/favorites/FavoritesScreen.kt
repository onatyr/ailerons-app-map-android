package fr.onat68.aileronsappmapandroid.favorites

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavController
import fr.onat68.aileronsappmapandroid.R
import fr.onat68.aileronsappmapandroid.data.entities.IndividualDTO
import fr.onat68.aileronsappmapandroid.species.IndividualItem

@Composable
fun FavoriteScreen(
    favoritesViewModel: FavoritesViewModel,
    navController: NavController
) {
    val favoritesList: State<List<IndividualDTO>> =
        favoritesViewModel.favoritesList.collectAsState(initial = listOf())

    Column {
        Text(LocalContext.current.resources.getString(R.string.favorites))
        for (favIndividual in favoritesList.value) {

            IndividualItem(favIndividual, navController)
            Divider()
        }
    }
}