package fr.onat68.aileronsappmapandroid.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import fr.onat68.aileronsappmapandroid.Constants
import fr.onat68.aileronsappmapandroid.R

@Composable
fun Header(label: String) {
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .height(IntrinsicSize.Min)
            .padding(horizontal = 10.dp, vertical = 4.dp)
    ) {
        Button(
            onClick = LocalPopBackStack.current,
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
            text = label,
            fontSize = 30.sp,
            fontFamily = LocalCustomFont.current,
            modifier = Modifier
        )
    }
}