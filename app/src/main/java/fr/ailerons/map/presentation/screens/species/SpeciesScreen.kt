package fr.ailerons.map.presentation.screens.species

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.res.stringResource
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import fr.ailerons.map.R
import fr.ailerons.map.presentation.IndividualScreenRoute
import fr.ailerons.map.presentation.screens.individual.IndividualViewModel

@Composable
fun SpeciesScreen(
    viewModel: IndividualViewModel = hiltViewModel(),
    navigateToIndividualScreen: (IndividualScreenRoute) -> Unit
) {
    val individualsList = viewModel.individualsList.collectAsState(initial = null)
    Column {
        Text(stringResource(R.string.individuals))
        IndividualsList(individualsList.value, navigateToIndividualScreen)
    }
}