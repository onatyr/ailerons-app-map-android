package fr.onat68.aileronsappmapandroid.individual

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import fr.onat68.aileronsappmapandroid.LocalCustomFont
import fr.onat68.aileronsappmapandroid.R
import fr.onat68.aileronsappmapandroid.map.Map
import fr.onat68.aileronsappmapandroid.map.MapViewModel

@Composable
fun IndividualScreen(
    individualId: Int,
    mapViewModel: MapViewModel,
    individualViewModel: IndividualViewModel
) {
    val scrollState = rememberScrollState()
    val individual =
        individualViewModel.individualsList.collectAsState(emptyList()).value.firstOrNull { it.id == individualId }

    individual?.let {
        Column {
            Header()
            Column(modifier = Modifier.verticalScroll(scrollState)) {

                Image(
                    painter = painterResource(R.drawable.raie),
                    contentDescription = "",
                    modifier = Modifier
                )

                Spacer(modifier = Modifier.size(20.dp))

                IndividualCharacteristics(individual, Modifier.align(Alignment.CenterHorizontally))

                Box(
                    modifier = Modifier
                        .height(300.dp)
                        .padding(8.dp)
                        .clip(RoundedCornerShape(16.dp))
                ) {
                    Map(mapViewModel, individualId)
                }

                Text(
                    text = "*Au moment de la pose de balise",
                    fontFamily = LocalCustomFont.current,
                    fontStyle = FontStyle.Italic,
                    fontWeight = FontWeight.Thin,
                    modifier = Modifier.align(Alignment.End).padding(horizontal = 8.dp)
                )
            }
        }
    }
}