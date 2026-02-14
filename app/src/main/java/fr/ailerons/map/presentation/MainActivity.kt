package fr.ailerons.map.presentation

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import dagger.hilt.android.AndroidEntryPoint
import fr.ailerons.map.data.entities.Article
import fr.ailerons.map.presentation.screens.individual.IndividualScreen
import fr.ailerons.map.presentation.screens.individual.IndividualViewModel
import fr.ailerons.map.presentation.lib.LocalCustomFont
import fr.ailerons.map.presentation.lib.LocalPopBackStack
import fr.ailerons.map.presentation.lib.atkinsonFontFamily
import fr.ailerons.map.presentation.screens.map.MapScreen
import fr.ailerons.map.presentation.screens.map.MapViewModel
import fr.ailerons.map.presentation.navBar.NavBar
import fr.ailerons.map.presentation.navBar.NavBarItem
import fr.ailerons.map.presentation.navBar.NavBarViewModel
import fr.ailerons.map.presentation.screens.map.rememberMapState
import fr.ailerons.map.presentation.screens.news.ArticleScreen
import fr.ailerons.map.presentation.screens.news.NewsScreen
import fr.ailerons.map.presentation.screens.news.NewsViewModel
import fr.ailerons.map.presentation.screens.species.SpeciesScreen
import kotlinx.serialization.Serializable

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            val navHostController = rememberNavController()
            CompositionLocalProvider(
                LocalCustomFont provides atkinsonFontFamily,
                LocalPopBackStack provides navHostController::popBackStack
            ) {
                Scaffold(
                    bottomBar = { NavBar(navigate = navHostController::navigate) }

                ) { innerPadding ->
                    val mapState = rememberMapState()

                    NavHost(
                        navController = navHostController,
                        startDestination = NavBarItem.Map.navRoute,
                        modifier = Modifier.padding(innerPadding)
                    ) {
                        composable<MapScreenRoute> {
                            MapScreen(
                                openIndividualSheet = { id ->
                                    navHostController.navigate(IndividualScreenRoute(id))
                                },
                                mapState = mapState
                            )
                        }
                        composable<SpeciesScreenRoute> {
                            ScreenSurface {
                                SpeciesScreen(navigateToIndividualScreen = navHostController::navigate)
                            }
                        }
                        composable<NewsScreenRoute> {
                            ScreenSurface {
                                NewsScreen(navigate = navHostController::navigate)
                            }
                        }
                        composable<Article> {
                            val article = it.toRoute<Article>()
                            ScreenSurface {
                                ArticleScreen(article = article)
                            }
                        }
                        composable<IndividualScreenRoute> {
                            ScreenSurface {
                                IndividualScreen(individualId = it.toRoute<IndividualScreenRoute>().individualId)
                            }
                        }
                    }
                }
            }
        }
    }
}

open class NavRoute

@Serializable
data class MapScreenRoute(val individualFilter: Int) : NavRoute()

@Serializable
data class IndividualScreenRoute(val individualId: Int) : NavRoute()

@Serializable
object NewsScreenRoute : NavRoute()

@Serializable
object SpeciesScreenRoute : NavRoute()

@Serializable
object SplashScreenRoute : NavRoute()



