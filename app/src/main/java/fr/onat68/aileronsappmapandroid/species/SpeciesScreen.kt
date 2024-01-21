package fr.onat68.aileronsappmapandroid.species

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import fr.onat68.aileronsappmapandroid.RecordPoints

@Composable
fun SpeciesScreen(individualsList: List<Individual>, navController: NavController) {
    Column {
        Text(text = "Hey")
        IndividualsList(individualsList, navController)
    }

}