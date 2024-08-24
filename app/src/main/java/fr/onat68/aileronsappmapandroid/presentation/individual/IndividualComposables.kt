package fr.onat68.aileronsappmapandroid.presentation.individual

import androidx.annotation.DrawableRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import fr.onat68.aileronsappmapandroid.Constants
import fr.onat68.aileronsappmapandroid.presentation.LocalCustomFont
import fr.onat68.aileronsappmapandroid.R
import fr.onat68.aileronsappmapandroid.data.entities.Individual

@Composable
fun Header() {
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .height(IntrinsicSize.Min)
            .padding(horizontal = 10.dp, vertical = 4.dp)
    ) {
        Button(
            onClick = { /*TODO*/ },
            contentPadding = PaddingValues(0.dp),
            colors = ButtonColors( // TODO pass them in LocalProvider
                containerColor = Color.Transparent,
                contentColor = Color.Transparent,
                disabledContentColor = Color.Transparent,
                disabledContainerColor = Color.Transparent
            )
        ) {
            Icon(
                painterResource(R.drawable.left_arrow),
                contentDescription = "Go back button",
                tint = Constants.ORANGE_COLOR,
                modifier = Modifier
            )
        }
        Text(
            text = stringResource(R.string.identity_card),
            fontSize = 30.sp,
            fontFamily = LocalCustomFont.current,
            modifier = Modifier
        )
    }
}

@Composable
fun IndividualCharacteristics(individual: Individual, modifier: Modifier) {
    Column {
        IndividualCharacteristicsHeader(
            individualName = individual.individualName,
            commonName = individual.commonName,
            binomialName = individual.binomialName
        )
        Row(
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .height(IntrinsicSize.Min)
        ) {
            Characteristic(
                modifier = Modifier.weight(1f),
                label = "Sexe",
                content = individual.sex,
                iconId = if (individual.sex == "Femelle") R.drawable.ic_female else R.drawable.ic_male
            )
            Characteristic(
                modifier = Modifier.weight(1f),
                label = "Envergure*",
                content = "${individual.size} mètre${if (individual.size > 1) "s" else ""}",
                iconId = R.drawable.ic_size
            )
            Characteristic(
                modifier = Modifier.weight(1.4f),
                label = "Situation de groupe*",
                content = individual.situation,
                iconId = R.drawable.ic_group_situation
            )
        }

        Column {
            Characteristic(
                label = "Comportement*",
                content = individual.behavior
            )
            Characteristic(
                label = "Description",
                content = individual.description
            )
            DateRange(startDate = "21/07/2023", endDate = "17/09/2023")
        }
    }
}

@Composable
fun IndividualCharacteristicsHeader(
    individualName: String,
    commonName: String,
    binomialName: String
) {
    Column(modifier = Modifier.padding(horizontal = 8.dp)) {
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = individualName,
                fontSize = 28.sp,
                fontFamily = LocalCustomFont.current,
                fontWeight = FontWeight.Bold
            )
            Column(horizontalAlignment = Alignment.End) {
                Text(text = commonName, fontFamily = LocalCustomFont.current, fontSize = 13.sp)
                Text(
                    text = binomialName,
                    fontStyle = FontStyle.Italic,
                    fontFamily = LocalCustomFont.current,
                    fontSize = 13.sp
                )
            }
        }
        HorizontalDivider(
            thickness = 1.dp,
            color = Color.Black,
            modifier = Modifier.padding(vertical = 5.dp)
        )
    }
}

@Composable
fun Characteristic(
    modifier: Modifier = Modifier,
    label: String,
    content: String,
    @DrawableRes iconId: Int? = null
) {
    Column(
        modifier = modifier
            .padding(horizontal = 8.dp, vertical = 5.dp)
            .fillMaxWidth()
    ) {
        Text(text = label, fontWeight = FontWeight.Thin, maxLines = 1)
        Spacer(
            modifier = Modifier
                .fillMaxWidth()
                .height(4.dp)
        )
        Row(verticalAlignment = Alignment.CenterVertically) {
            iconId?.let { id ->
                Icon(
                    painter = painterResource(id),
                    contentDescription = null,
                    modifier = Modifier.padding(2.dp)
                )
            }
            Text(text = content)
        }
    }
}

@Composable
fun DateRange(startDate: String, endDate: String) {
    val dateRangeString = buildAnnotatedString {
        withStyle(style = SpanStyle(fontWeight = FontWeight.Thin)) {
            append("Parcours individuel du ")
        }
        append(startDate)
        withStyle(style = SpanStyle(fontWeight = FontWeight.Thin)) {
            append(" au ")
        }
        append(endDate)
    }

    Text(
        text = dateRangeString,
        modifier = Modifier
            .padding(horizontal = 8.dp, vertical = 5.dp)
            .fillMaxWidth()
    )
}