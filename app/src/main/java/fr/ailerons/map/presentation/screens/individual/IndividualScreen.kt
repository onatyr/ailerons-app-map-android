package fr.ailerons.map.presentation.screens.individual

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import fr.ailerons.map.R
import fr.ailerons.map.data.entities.Individual
import fr.ailerons.map.presentation.ScrollableColumnWithHeader
import fr.ailerons.map.presentation.lib.LocalCustomFont
import fr.ailerons.map.presentation.screens.map.rememberMapGestureHandler

@Composable
fun IndividualScreen(
    individualId: Int,
    viewModel: IndividualViewModel = hiltViewModel()
) {
    val mapGestureHandler = rememberMapGestureHandler()
    val individual =
        viewModel.individualsList.collectAsState(emptyList()).value.firstOrNull { it.id == individualId }

    individual?.let {
        ScrollableColumnWithHeader(
            headerLabel = stringResource(R.string.identity_card),
            mapGestureHandler = mapGestureHandler
        ) {
            Spacer(modifier = Modifier.size(20.dp))
            IndividualSheet(individual)
        }
    }
}

@Composable
fun IndividualSheet(individual: Individual, recordTimestamp: String? = null) {
    Column {
        IndividualCharacteristics(individual = individual, recordTimestamp = recordTimestamp)

        Image(
            painter = painterResource(R.drawable.raie),
            contentDescription = "Image of ${individual.individualName}",
            modifier = Modifier
                .wrapContentHeight()
                .padding(8.dp)
                .clip(RoundedCornerShape(16.dp))
        )

        Row(
            horizontalArrangement = Arrangement.End,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp)
        ) {
            Text(
                text = "*Au moment de la pose de balise",
                fontFamily = LocalCustomFont.current,
                fontStyle = FontStyle.Italic,
                fontWeight = FontWeight.Thin,
                modifier = Modifier.padding(horizontal = 8.dp)
            )
        }
    }
}