package fr.onat68.aileronsappmapandroid.species

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavController
import fr.onat68.aileronsappmapandroid.R
import fr.onat68.aileronsappmapandroid.individual.Individual

@Composable
fun SpeciesScreen(individualsList: List<Individual>, navController: NavController) {
    Column {
        Text(LocalContext.current.resources.getString(R.string.species))
        IndividualsList(individualsList, navController)
    }
}