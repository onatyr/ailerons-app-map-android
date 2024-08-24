package fr.onat68.aileronsappmapandroid.species

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.size
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.core.graphics.drawable.toBitmap
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import fr.onat68.aileronsappmapandroid.IndividualScreenRoute
import fr.onat68.aileronsappmapandroid.R
import fr.onat68.aileronsappmapandroid.data.entities.Individual
import fr.onat68.aileronsappmapandroid.data.entities.IndividualDTO

@Composable
fun IndividualsList(individualsList: List<Individual>?, navHostController: NavHostController) {
    individualsList ?:return
    for (i in individualsList.indices) {
        Column {
            IndividualItem(individualsList[i], navHostController)
            HorizontalDivider(Modifier.size(5.dp))
        }
    }
}

@Composable
fun IndividualItem(individual: Individual, navController: NavController) {
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
        modifier = Modifier.clickable { navController.navigate(IndividualScreenRoute(individual.id)) }
    )
}


