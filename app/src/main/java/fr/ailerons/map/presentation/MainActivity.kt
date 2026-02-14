package fr.ailerons.map.presentation

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import dagger.hilt.android.AndroidEntryPoint
import fr.ailerons.map.R
import fr.ailerons.map.data.entities.Article
import fr.ailerons.map.presentation.individual.IndividualScreen
import fr.ailerons.map.presentation.individual.IndividualViewModel
import fr.ailerons.map.presentation.map.MapScreen
import fr.ailerons.map.presentation.map.MapViewModel
import fr.ailerons.map.presentation.navBar.NavBar
import fr.ailerons.map.presentation.navBar.NavBarItem
import fr.ailerons.map.presentation.navBar.NavBarViewModel
import fr.ailerons.map.presentation.news.ArticleScreen
import fr.ailerons.map.presentation.news.NewsScreen
import fr.ailerons.map.presentation.news.NewsViewModel
import fr.ailerons.map.presentation.species.SpeciesScreen
import kotlinx.serialization.Serializable

val LocalCustomFont = staticCompositionLocalOf {
    FontFamily(
        Font(R.font.atkinson_hyperlegible_regular, FontWeight.Normal),
        Font(R.font.atkinson_hyperlegible_italic, style = FontStyle.Italic),
        Font(R.font.atkinson_hyperlegible_bold, FontWeight.Bold)
    )
}

val LocalPopBackStack = compositionLocalOf { {} }

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val individualViewModel: IndividualViewModel by viewModels()
    private val mapViewModel: MapViewModel by viewModels()
    private val navBarViewModel: NavBarViewModel by viewModels()
    private val newsViewModel: NewsViewModel by viewModels()


    private val atkinsonFontFamily = FontFamily(
        Font(R.font.atkinson_hyperlegible_regular, FontWeight.Normal),
        Font(R.font.atkinson_hyperlegible_bold, FontWeight.Bold)
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {

            val navHostController = rememberNavController()


            CompositionLocalProvider(
                LocalCustomFont provides atkinsonFontFamily,
                LocalPopBackStack provides navHostController::popBackStack
            ) {

                Column {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .weight(1f)
                    ) {
                        MapScreen(mapViewModel) { id ->
                            navHostController.navigate(IndividualScreenRoute(id))
                        }
                        NavHost(
                            navController = navHostController,
                            startDestination = NavBarItem.Map.navRoute,
                        ) {
                            composable<MapScreenRoute> {}
                            composable<SpeciesScreenRoute> {
                                ScreenSurface {
                                    SpeciesScreen(
                                        individualViewModel,
                                        navHostController
                                    )
                                }
                            }
                            composable<NewsScreenRoute> {
                                ScreenSurface {
                                    NewsScreen(
                                        newsViewModel,
                                        {},//NavController::navigate
                                    )
                                }
                            }
                            composable<Article> {
                                val article = it.toRoute<Article>()
                                ScreenSurface {
                                    ArticleScreen(article)
                                }
                            }
                            composable<IndividualScreenRoute> {
                                ScreenSurface {
                                    IndividualScreen(
                                        it.toRoute<IndividualScreenRoute>().individualId,
                                        mapViewModel,
                                        individualViewModel
                                    )
                                }
                            }
                        }
                    }
                    NavBar(navBarViewModel, {})//NavController::navigate)
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



