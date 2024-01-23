package fr.onat68.aileronsappmapandroid.individual

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import fr.onat68.aileronsappmapandroid.favorites.FavoritesViewModel
import fr.onat68.aileronsappmapandroid.map.Map
import fr.onat68.aileronsappmapandroid.map.MapViewModel
import fr.onat68.aileronsappmapandroid.species.Individual

@Composable
fun IndividualScreen(
    individual: Individual,
    mapViewModel: MapViewModel,
    favoritesViewModel: FavoritesViewModel
) {
    val favoritesList = favoritesViewModel.favoritesList.collectAsState(initial = listOf())
    val liked = favoritesList.value.contains(individual)

    Column {
        Row(
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(individual.individualName, fontSize = 40.sp)
            FavToggle(liked, individual.individualRecordId, favoritesViewModel::changeFav)
        }
        Spacer(modifier = Modifier.size(20.dp))
        Text(
            "${individual.commonName} / ${individual.binomialName}",
            fontStyle = FontStyle.Italic,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )
        Text(
            "Age: ${individual.age} Poids: ${individual.weight} Sexe: ${individual.sex}",
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )
        Spacer(modifier = Modifier.size(60.dp))
        Box(modifier = Modifier.height(300.dp)) {
            Map(mapViewModel, individual.id)
        }
    }
}