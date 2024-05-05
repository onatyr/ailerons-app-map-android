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
import fr.onat68.aileronsappmapandroid.favorites.IndividualViewModel
import fr.onat68.aileronsappmapandroid.map.Map
import fr.onat68.aileronsappmapandroid.map.MapViewModel

@Composable
fun IndividualScreen(
    individualId: Int,
    mapViewModel: MapViewModel,
    individualViewModel: IndividualViewModel
) {
    val individual = individualViewModel.individualsList.collectAsState(emptyList()).value.firstOrNull() { it.id == individualId }
    if(individual != null) {

        Column {
            Header(individual, individualViewModel::changeFav)

            Spacer(modifier = Modifier.size(20.dp))

            IndividualCharacteristics(individual, Modifier.align(Alignment.CenterHorizontally))

            Spacer(modifier = Modifier.size(20.dp))

            Box(modifier = Modifier.height(300.dp)) {
                Map(mapViewModel, individualId)
            }
        }
    }
}