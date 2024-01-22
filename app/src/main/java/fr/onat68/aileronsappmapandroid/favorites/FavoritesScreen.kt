package fr.onat68.aileronsappmapandroid.favorites

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import fr.onat68.aileronsappmapandroid.species.Individual
import fr.onat68.aileronsappmapandroid.species.IndividualItem

@Composable
fun FavoriteScreen(
    favoritesViewModel: FavoritesViewModel,
    navController: NavController,
    individualsList: List<Individual>
) {

    val favoritesList: State<List<Favorite>> =
        favoritesViewModel.favoritesList.collectAsState(initial = listOf())

    Column {
        for (fav in favoritesList.value) {

            val favoriteIndividual =
                individualsList.first { it.individualRecordId == fav.individualId }
            val individualListId = individualsList.indexOf(favoriteIndividual)

            IndividualItem(favoriteIndividual, navController, individualListId)
            Divider()
        }
    }
}