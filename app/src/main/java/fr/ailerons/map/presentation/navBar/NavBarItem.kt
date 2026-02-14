package fr.ailerons.map.presentation.navBar

import fr.ailerons.map.Constants
import fr.ailerons.map.presentation.MapScreenRoute
import fr.ailerons.map.presentation.NavRoute
import fr.ailerons.map.presentation.NewsScreenRoute
import fr.ailerons.map.presentation.SpeciesScreenRoute
import fr.ailerons.map.R

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