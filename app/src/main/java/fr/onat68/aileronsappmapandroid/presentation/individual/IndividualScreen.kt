package fr.onat68.aileronsappmapandroid.presentation.individual

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import fr.onat68.aileronsappmapandroid.R
import fr.onat68.aileronsappmapandroid.presentation.LocalCustomFont
import fr.onat68.aileronsappmapandroid.presentation.ScrollableColumnWithHeader
import fr.onat68.aileronsappmapandroid.presentation.map.MapScreen
import fr.onat68.aileronsappmapandroid.presentation.map.MapViewModel
import fr.onat68.aileronsappmapandroid.presentation.map.rememberMapGestureHandler

@Composable
fun IndividualScreen(
    individualId: Int,
    mapViewModel: MapViewModel,
    individualViewModel: IndividualViewModel
) {
    val mapGestureHandler = rememberMapGestureHandler()
    val individual =
        individualViewModel.individualsList.collectAsState(emptyList()).value.firstOrNull { it.id == individualId }

    individual?.let {
        ScrollableColumnWithHeader(
            headerLabel = stringResource(R.string.identity_card),
            mapGestureHandler = mapGestureHandler
        ) {
            Image(
                painter = painterResource(R.drawable.raie),
                contentDescription = "",
            )

            Spacer(modifier = Modifier.size(20.dp))

            IndividualCharacteristics(individual)

            Box(
                modifier = Modifier
                    .height(300.dp)
                    .padding(8.dp)
                    .clip(RoundedCornerShape(16.dp))

            ) {
                MapScreen(mapViewModel, individualId, mapGestureHandler)
            }

            Text(
                text = "*Au moment de la pose de balise",
                fontFamily = LocalCustomFont.current,
                fontStyle = FontStyle.Italic,
                fontWeight = FontWeight.Thin,
                modifier = Modifier
                    .align(Alignment.End)
                    .padding(horizontal = 8.dp)
            )
        }
    }
}

@Composable
fun MiniMapCard(mapViewModel: MapViewModel, individualId: Int) {

}