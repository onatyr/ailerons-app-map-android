package fr.onat68.aileronsappmapandroid.individual

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import fr.onat68.aileronsappmapandroid.favorites.FavoritesViewModel
import fr.onat68.aileronsappmapandroid.map.Map
import fr.onat68.aileronsappmapandroid.map.MapViewModel

@Composable
fun IndividualScreen(
    individual: Individual,
    mapViewModel: MapViewModel,
    favoritesViewModel: FavoritesViewModel
) {
    val favoritesList = favoritesViewModel.favoritesList.collectAsState(initial = listOf())
    val liked = favoritesList.value.contains(individual)

    Column {
        Header(individual, liked, favoritesViewModel::changeFav)

        Spacer(modifier = Modifier.size(20.dp))

        IndividualCharacteristics(individual, Modifier.align(Alignment.CenterHorizontally))

        Spacer(modifier = Modifier.size(20.dp))

        Box(modifier = Modifier.height(300.dp)) {
            Map(mapViewModel, individual.id)
        }
    }
}