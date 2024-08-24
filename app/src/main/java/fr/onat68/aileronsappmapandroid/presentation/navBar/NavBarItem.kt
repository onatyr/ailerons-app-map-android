package fr.onat68.aileronsappmapandroid.presentation.navBar

import fr.onat68.aileronsappmapandroid.Constants
import fr.onat68.aileronsappmapandroid.presentation.MapScreenRoute
import fr.onat68.aileronsappmapandroid.presentation.NavRoute
import fr.onat68.aileronsappmapandroid.presentation.NewsScreenRoute
import fr.onat68.aileronsappmapandroid.R
import fr.onat68.aileronsappmapandroid.presentation.SpeciesScreenRoute

sealed class NavBarItem(
    val title: String,
    val icon: Int,
    val navRoute: NavRoute
) {

    companion object {
        fun values() = listOf(News, Map, Individuals)
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