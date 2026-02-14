package fr.ailerons.map.presentation.species

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import fr.ailerons.map.presentation.individual.IndividualViewModel
import fr.ailerons.map.R

@Composable
fun SpeciesScreen(individualViewModel: IndividualViewModel, navHostController: NavHostController) {
    val individualsList = individualViewModel.individualsList.collectAsState(initial = null)
    Column {
        Text(LocalContext.current.resources.getString(R.string.individuals))
        IndividualsList(individualsList.value, navHostController)
    }
}