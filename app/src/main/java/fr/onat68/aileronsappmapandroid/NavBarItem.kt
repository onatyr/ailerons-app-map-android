package fr.onat68.aileronsappmapandroid

import androidx.compose.ui.graphics.vector.ImageVector

sealed class NavBarItem(
    var title: String,
    var icon: Int,
    var route: String
) {
    object Favorites:
            NavBarItem(
                "Favoris",
                R.drawable.ic_fav_foreground,
                "favorites"
            )
    object Map:
            NavBarItem(
                "Carte",
                R.drawable.ic_map_foreground,
                "map"
            )
    object Species:
            NavBarItem(
                "Espèces",
                R.drawable.ic_list_foreground,
                "species"
            )
    object News:
            NavBarItem(
                "Actualités",
                R.drawable.ic_news_foreground,
                "news"
            )
}