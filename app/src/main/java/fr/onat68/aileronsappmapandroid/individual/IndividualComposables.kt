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
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import fr.onat68.aileronsappmapandroid.R
import fr.onat68.aileronsappmapandroid.ui.theme.atkinsonHyperlegible

@Composable
fun Header(individual: Individual, liked: Boolean, changeFav: (Int) -> Unit) {
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            individual.individualName,
            fontFamily = atkinsonHyperlegible,
            fontWeight = FontWeight.Bold,
            fontSize = 40.sp,
            color = Color.White,
            modifier = Modifier
                .weight(1f)
                .padding(15.dp)
        )
        FavToggle(liked, individual.individualRecordId, changeFav)
        Spacer(modifier = Modifier.size(15.dp))
    }
}

@Composable
fun FavToggle(liked: Boolean, individualId: Int, changeFav: (Int) -> Unit) {

    OutlinedButton(onClick = { changeFav(individualId) }) {
        val imageVector = if (liked) {
            ImageVector.vectorResource(R.drawable.ic_star)
        } else {
            ImageVector.vectorResource(R.drawable.ic_unstar)
        }

        Icon(
            imageVector = imageVector,
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
    Text(
        "${LocalContext.current.resources.getString(R.string.age)}: ${individual.age} ${
            LocalContext.current.resources.getString(
                R.string.weight
            )
        }: ${individual.weight} ${LocalContext.current.resources.getString(R.string.sex)}: ${individual.sex}",
        modifier = modifier
    )
}