package fr.onat68.aileronsappmapandroid.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.ProvidableCompositionLocal
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import dagger.hilt.android.AndroidEntryPoint
import fr.onat68.aileronsappmapandroid.R
import fr.onat68.aileronsappmapandroid.data.entities.Article
import fr.onat68.aileronsappmapandroid.presentation.article.ArticleScreen
import fr.onat68.aileronsappmapandroid.presentation.individual.IndividualViewModel
import fr.onat68.aileronsappmapandroid.presentation.individual.IndividualScreen
import fr.onat68.aileronsappmapandroid.presentation.map.Map
import fr.onat68.aileronsappmapandroid.presentation.map.MapViewModel
import fr.onat68.aileronsappmapandroid.presentation.navBar.NavBar
import fr.onat68.aileronsappmapandroid.presentation.navBar.NavBarItem
import fr.onat68.aileronsappmapandroid.presentation.navBar.NavBarViewModel
import fr.onat68.aileronsappmapandroid.presentation.news.NewsScreen
import fr.onat68.aileronsappmapandroid.presentation.news.NewsViewModel
import fr.onat68.aileronsappmapandroid.presentation.species.SpeciesScreen
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
class MainActivity : ComponentActivity() {

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
                    NavHost(
                        navController = navHostController,
                        startDestination = NavBarItem.Map.navRoute,
                        modifier = Modifier.weight(1f)
                    ) {
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
                        composable<NewsScreenRoute> {
                            NewsScreen(
                                newsViewModel,
                                navHostController::navigate
                            )
                        }
                        composable<Article> {
                            val article = it.toRoute<Article>()
                            ArticleScreen(article)
                        }
                        composable<IndividualScreenRoute> {
                            IndividualScreen(
                                it.toRoute<IndividualScreenRoute>().individualId,
                                mapViewModel,
                                individualViewModel
                            )
                        }
                    }
                    NavBar(navBarViewModel, navHostController::navigate)
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
data class ArticleScreenRoute(val article: Article) : NavRoute()

@Serializable
object NewsScreenRoute : NavRoute()

@Serializable
object SpeciesScreenRoute : NavRoute()



