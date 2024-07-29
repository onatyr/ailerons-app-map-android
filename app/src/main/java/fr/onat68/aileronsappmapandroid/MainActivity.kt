package fr.onat68.aileronsappmapandroid

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import dagger.hilt.android.AndroidEntryPoint
import fr.onat68.aileronsappmapandroid.favorites.FavoriteScreen
import fr.onat68.aileronsappmapandroid.favorites.IndividualViewModel
import fr.onat68.aileronsappmapandroid.individual.IndividualScreen
import fr.onat68.aileronsappmapandroid.map.Map
import fr.onat68.aileronsappmapandroid.map.MapViewModel
import fr.onat68.aileronsappmapandroid.news.NewsScreen
import fr.onat68.aileronsappmapandroid.species.SpeciesScreen
import fr.onat68.aileronsappmapandroid.ui.theme.AileronsAppMapAndroidTheme
import kotlinx.serialization.Serializable


@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val individualViewModel: IndividualViewModel by viewModels()
    private val mapViewModel: MapViewModel by viewModels()
    private val navBarViewModel: NavBarViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {

            AileronsAppMapAndroidTheme {

                val navHostController = rememberNavController()


                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Column {
                        NavHost(
                            navController = navHostController,
                            startDestination = NavBarItem.Map.navRoute,
                            modifier = Modifier.weight(1f)
                        ) {
                            composable<FavoritesScreenRoute> {
                                FavoriteScreen(
                                    individualViewModel,
                                    navHostController
                                )
                            }
                            composable<MapScreenRoute> {
                                Map(
                                    mapViewModel,
                                    it.toRoute<MapScreenRoute>().individualFilter
                                )
                            }
                            composable<SpeciesScreenRoute> {
                                SpeciesScreen(
                                    individualViewModel,
                                    navHostController
                                )
                            }
                            composable<NewsScreenRoute> { NewsScreen() }
                            composable<IndividualScreenRoute> {
                                IndividualScreen(
                                    it.toRoute<IndividualScreenRoute>().individualId,
                                    mapViewModel,
                                    individualViewModel
                                )
                            }
                        }
                        NavBar(navBarViewModel, navHostController)
                    }
                }
            }
        }
    }
}

open class NavRoute

@Serializable
object FavoritesScreenRoute : NavRoute()

@Serializable
data class MapScreenRoute(val individualFilter: Int) : NavRoute()

@Serializable
data class IndividualScreenRoute(val individualId: Int) : NavRoute()

@Serializable
object NewsScreenRoute : NavRoute()

@Serializable
object SpeciesScreenRoute : NavRoute()


