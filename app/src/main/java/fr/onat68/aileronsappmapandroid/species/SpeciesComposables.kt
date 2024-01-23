package fr.onat68.aileronsappmapandroid.species

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import fr.onat68.aileronsappmapandroid.individual.Individual

@Composable
fun IndividualsList(individualsList: List<Individual>, navController: NavController) {
    for (i in individualsList.indices) {
        Column {
            IndividualItem(individualsList[i], navController)
            Divider()
        }
    }
}
@Composable
fun IndividualItem(individual: Individual, navController: NavController){
    ListItem(
        headlineContent = { Text(individual.individualName) },
        supportingContent = {
            Text(individual.binomialName)
        },
        leadingContent = {
            Icon(
                Icons.Filled.Favorite,
                contentDescription = "Localized description",
            )
        },
        trailingContent = { Text("meta") },
        modifier = Modifier.clickable { navController.navigate("individualSheet/${individual.individualRecordId}") }
    )
}


