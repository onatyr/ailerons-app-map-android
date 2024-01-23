package fr.onat68.aileronsappmapandroid.news

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import fr.onat68.aileronsappmapandroid.R

@Composable
fun NewsScreen() {
    Text(LocalContext.current.resources.getString(R.string.news))
}