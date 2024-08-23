package fr.onat68.aileronsappmapandroid.favorites

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.size
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import fr.onat68.aileronsappmapandroid.R
import fr.onat68.aileronsappmapandroid.data.entities.Individual
import fr.onat68.aileronsappmapandroid.species.IndividualItem

@Composable
fun FavoriteScreen(
    favoritesViewModel: IndividualViewModel,
    navController: NavController
) {
    val favoritesList: State<List<Individual>> =
        favoritesViewModel.favoritesList.collectAsState(initial = listOf())

    Column {
        for (favIndividual in favoritesList.value) {

            IndividualItem(favIndividual, navController)
            HorizontalDivider(Modifier.size(5.dp))
        }
    }
}