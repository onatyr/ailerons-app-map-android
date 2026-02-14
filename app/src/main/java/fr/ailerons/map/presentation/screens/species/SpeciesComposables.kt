package fr.ailerons.map.presentation.screens.species

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.size
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import fr.ailerons.map.data.entities.Individual
import fr.ailerons.map.presentation.IndividualScreenRoute
import fr.ailerons.map.R

@Composable
fun IndividualsList(
    individualsList: List<Individual>?,
    navigateToIndividualScreen: (IndividualScreenRoute) -> Unit
) {
    individualsList ?: return
    for (individual in individualsList) {
        Column {
            IndividualItem(individual, navigateToIndividualScreen)
            HorizontalDivider(Modifier.size(5.dp))
        }
    }
}

@Composable
fun IndividualItem(
    individual: Individual,
    navigateToIndividualScreen: (IndividualScreenRoute) -> Unit
) {
    ListItem(
        headlineContent = { Text(individual.individualName, fontWeight = FontWeight.Bold) },
        supportingContent = {
            Text(individual.binomialName)
        },
        leadingContent = {
            Icon(
                painter = painterResource(id = R.drawable.ic_shark),
                contentDescription = "Shark Icon",
                modifier = Modifier.size(30.dp)
            )
        },
        trailingContent = { Text("meta") },
        modifier = Modifier.clickable { navigateToIndividualScreen(IndividualScreenRoute(individual.id)) }
    )
}


