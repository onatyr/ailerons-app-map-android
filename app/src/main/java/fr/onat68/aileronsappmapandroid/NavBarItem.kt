package fr.onat68.aileronsappmapandroid

sealed class NavBarItem(
    val title: String,
    val icon: Int,
    val route: String
) {

    companion object {
        fun values() = listOf(Favorites, Map, Species, News)
    }
    data object Favorites:
            NavBarItem(
                "Favoris",
                R.drawable.ic_star,
                "favorites"
            )
    data object Map:
            NavBarItem(
                "Carte",
                R.drawable.ic_map,
                "map/${Constants.defaultFilter}"
            )
    data object Species:
            NavBarItem(
                "Espèces",
                R.drawable.ic_species,
                "species"
            )
    data object News:
            NavBarItem(
                "Actualités",
                R.drawable.ic_news,
                "news"
            )
}