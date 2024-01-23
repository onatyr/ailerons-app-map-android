package fr.onat68.aileronsappmapandroid.individual

import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import fr.onat68.aileronsappmapandroid.R

@Composable
fun FavToggle(liked: Boolean, individualId: Int, changeFav: (Int) -> Unit) {

    Button(onClick = { changeFav(individualId) }) {
        if (liked) {
            Icon(
                imageVector = ImageVector.vectorResource(R.drawable.ic_star),
                contentDescription = "Add to favorites"
            )
        } else {
            Icon(
                imageVector = ImageVector.vectorResource(R.drawable.ic_unstar),
                contentDescription = "Add to favorites"
            )
        }
    }
}