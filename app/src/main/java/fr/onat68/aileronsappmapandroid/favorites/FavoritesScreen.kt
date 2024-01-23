package fr.onat68.aileronsappmapandroid.favorites

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Divider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.navigation.NavController
import fr.onat68.aileronsappmapandroid.individual.Individual
import fr.onat68.aileronsappmapandroid.species.IndividualItem

@Composable
fun FavoriteScreen(
    favoritesViewModel: FavoritesViewModel,
    navController: NavController
) {

    val favoritesList: State<List<Individual>> =
        favoritesViewModel.favoritesList.collectAsState(initial = listOf())

    Column {
        for (favIndividual in favoritesList.value) {

            IndividualItem(favIndividual, navController)
            Divider()
        }
    }
}