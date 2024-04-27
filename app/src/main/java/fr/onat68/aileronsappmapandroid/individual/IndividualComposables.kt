package fr.onat68.aileronsappmapandroid.individual

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import fr.onat68.aileronsappmapandroid.R
import fr.onat68.aileronsappmapandroid.data.entities.Individual
import fr.onat68.aileronsappmapandroid.data.entities.IndividualDTO
import fr.onat68.aileronsappmapandroid.ui.theme.atkinsonHyperlegible

@Composable
fun Header(individual: Individual, changeFav: (Individual) -> Unit) {
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            individual.name,
            fontFamily = atkinsonHyperlegible,
            fontWeight = FontWeight.Bold,
            fontSize = 40.sp,
            color = Color.White,
            modifier = Modifier
                .weight(1f)
                .padding(15.dp)
        )
        FavToggle(individual, changeFav)
        Spacer(modifier = Modifier.size(15.dp))
    }
}

@Composable
fun FavToggle(individual: Individual, changeFav: (Individual) -> Unit) {

    OutlinedButton(onClick = { changeFav(individual) }) {
        Icon(
            painter = if (individual.isFavorite) painterResource(R.drawable.ic_star) else painterResource(R.drawable.ic_unstar),
            contentDescription = "Add to favorites"
        )
    }
}

@Composable
fun IndividualCharacteristics(individual: Individual, modifier: Modifier) {

    Text(
        "${individual.commonName} / ${individual.binomialName}",
        fontStyle = FontStyle.Italic,
        modifier = modifier
    )
}