package fr.onat68.aileronsappmapandroid.species

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.graphics.drawable.toBitmap
import androidx.navigation.NavController
import fr.onat68.aileronsappmapandroid.R
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
fun IndividualItem(individual: Individual, navController: NavController) {
    val sharkImageBitmap = LocalContext.current.getDrawable(R.drawable.ic_shark)!!.toBitmap().asImageBitmap()
    val rayImageBitmap = LocalContext.current.getDrawable(R.drawable.ic_ray_manta)!!.toBitmap().asImageBitmap()
    val iconList = listOf(sharkImageBitmap, rayImageBitmap)

    ListItem(
        headlineContent = { Text(individual.individualName) },
        supportingContent = {
            Text(individual.binomialName)
        },
        leadingContent = {
                         Icon(
                             bitmap = iconList[individual.icon-1], contentDescription = "Shark Icon", modifier = Modifier.size(30.dp)
                         )
//            Icon(
//                Icons.Filled.Favorite,
//                contentDescription = "Localized description",
//            )
        },
        trailingContent = { Text("meta") },
        modifier = Modifier.clickable { navController.navigate("individualSheet/${individual.individualRecordId}") }
    )
}


