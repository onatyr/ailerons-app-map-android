package fr.onat68.aileronsappmapandroid

sealed class NavBarItem(
    val title: String,
    val icon: Int,
    val navRoute: NavRoute
) {

    companion object {
        fun values() = listOf(Favorites, Map, Species, News)
    }
    data object Favorites:
            NavBarItem(
                "Favoris",
                R.drawable.ic_star,
                FavoritesScreenRoute
            )
    data object Map:
            NavBarItem(
                "Carte",
                R.drawable.ic_map,
                MapScreenRoute(Constants.DEFAULT_FILTER)
            )
    data object Species:
            NavBarItem(
                "Espèces",
                R.drawable.ic_species,
                SpeciesScreenRoute
            )
    data object News:
            NavBarItem(
                "Actualités",
                R.drawable.ic_news,
                NewsScreenRoute
            )
}