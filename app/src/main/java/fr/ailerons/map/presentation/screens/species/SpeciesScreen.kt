package fr.ailerons.map.presentation.screens.species

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import fr.ailerons.map.presentation.screens.individual.IndividualViewModel
import fr.ailerons.map.R
import fr.ailerons.map.presentation.IndividualScreenRoute

@Composable
fun SpeciesScreen(
    viewModel: IndividualViewModel = hiltViewModel(),
    navigateToIndividualScreen: (IndividualScreenRoute) -> Unit
) {
    val individualsList = viewModel.individualsList.collectAsState(initial = null)
    Column {
        Text(LocalContext.current.resources.getString(R.string.individuals))
        IndividualsList(individualsList.value, navigateToIndividualScreen)
    }
}