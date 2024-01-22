package fr.onat68.aileronsappmapandroid

sealed class NavBarItem(
    var title: String,
    var icon: Int,
    var route: String
) {
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