package fr.onat68.aileronsappmapandroid

import androidx.compose.ui.graphics.vector.ImageVector

sealed class NavBarItem(
    var title: String,
    var icon: Int
) {
    object Favorites:
            NavBarItem(
                "Favoris",
                R.drawable.ic_fav_foreground
            )
    object Map:
            NavBarItem(
                "Carte",
                R.drawable.ic_map_foreground
            )
    object Species:
            NavBarItem(
                "Espèces",
                R.drawable.ic_list_foreground
            )
    object News:
            NavBarItem(
                "Actualités",
                R.drawable.ic_news_foreground
            )
}