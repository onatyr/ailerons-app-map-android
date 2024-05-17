package fr.onat68.aileronsappmapandroid.species

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import fr.onat68.aileronsappmapandroid.R
import fr.onat68.aileronsappmapandroid.data.entities.Individual
import fr.onat68.aileronsappmapandroid.data.entities.IndividualDTO
import fr.onat68.aileronsappmapandroid.favorites.IndividualViewModel

@Composable
fun SpeciesScreen(individualViewModel: IndividualViewModel, navHostController: NavHostController) {
    val individualsList = individualViewModel.individualsList.collectAsState(initial = null)
    Column {
        Text(LocalContext.current.resources.getString(R.string.species))
        IndividualsList(individualsList.value, navHostController)
    }
}