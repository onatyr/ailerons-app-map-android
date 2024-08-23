package fr.onat68.aileronsappmapandroid

sealed class NavBarItem(
    val title: String,
    val icon: Int,
    val navRoute: NavRoute
) {

    companion object {
        fun values() = listOf(Individuals, Map, News)
    }
    data object Map:
            NavBarItem(
                "Carte",
                R.drawable.ic_map,
                MapScreenRoute(Constants.DEFAULT_FILTER)
            )
    data object Individuals:
            NavBarItem(
                "Individus",
                R.drawable.ic_individual,
                SpeciesScreenRoute
            )
    data object News:
            NavBarItem(
                "Actus",
                R.drawable.ic_news,
                NewsScreenRoute
            )
}