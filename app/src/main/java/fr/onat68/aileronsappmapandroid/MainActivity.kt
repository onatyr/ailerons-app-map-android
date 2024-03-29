package fr.onat68.aileronsappmapandroid

import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import fr.onat68.aileronsappmapandroid.favorites.AppDatabase
import fr.onat68.aileronsappmapandroid.favorites.FavoriteScreen
import fr.onat68.aileronsappmapandroid.favorites.FavoritesViewModel
import fr.onat68.aileronsappmapandroid.individual.Individual
import fr.onat68.aileronsappmapandroid.individual.IndividualScreen
import fr.onat68.aileronsappmapandroid.map.Map
import fr.onat68.aileronsappmapandroid.map.MapViewModel
import fr.onat68.aileronsappmapandroid.news.NewsScreen
import fr.onat68.aileronsappmapandroid.species.SpeciesScreen
import fr.onat68.aileronsappmapandroid.ui.theme.AileronsAppMapAndroidTheme
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.postgrest.Postgrest
import io.github.jan.supabase.postgrest.from
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.withContext


class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        val ai: ApplicationInfo = applicationContext.packageManager
            .getApplicationInfo(applicationContext.packageName, PackageManager.GET_META_DATA)
        @Suppress("DEPRECATION") val supabase = createSupabaseClient(
            supabaseUrl = ai.metaData["supabaseUrl"].toString(),
            supabaseKey = ai.metaData["supabaseKey"].toString(),
        ) {
            install(Postgrest)
        }
        setContent {


            AileronsAppMapAndroidTheme {

                val database = AppDatabase.getInstance(this)
                val navController = rememberNavController()

                val _recordsPoints = MutableStateFlow<List<RecordPoint>>(listOf())
                val recordPoints: Flow<List<RecordPoint>> = _recordsPoints

                val navBarViewModel = NavBarViewModel(navController)

                val mapViewModel = MapViewModel(recordPoints, navController, navBarViewModel)
                lateinit var favoritesViewModel: FavoritesViewModel

                var individualsList by remember { mutableStateOf<List<Individual>>(listOf()) }


                LaunchedEffect(Unit) { // Fetch the data from Supabase
                    withContext(Dispatchers.IO) {

                        _recordsPoints.value = supabase.from("record")
                            .select().decodeList<RecordPoint>().sortedBy { it.recordTimestamp }

                        individualsList = supabase.from("individual")
                            .select().decodeList<Individual>()
                        favoritesViewModel = FavoritesViewModel(database, individualsList)
                    }
                }

                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Column {
                        NavHost(
                            navController = navController,
                            startDestination = "map/${Constants.defaultFilter}",
                            modifier = Modifier.weight(1f)
                        ) {
                            composable("favorites") {
                                FavoriteScreen(
                                    favoritesViewModel,
                                    navController
                                )
                            }
                            composable(
                                "map/{individualIdFilter}",
                                arguments = listOf(navArgument("individualIdFilter") {
                                    type = NavType.IntType
                                })
                            ) {
                                val individualIdFilter =
                                    it.arguments!!.getInt("individualIdFilter")
                                Map(mapViewModel, individualIdFilter)

                            }
                            composable("species") {
                                SpeciesScreen(
                                    individualsList,
                                    navController
                                )
                            }
                            composable("news") { NewsScreen() }
                            composable(
                                "individualSheet/{listId}",
                                arguments = listOf(navArgument("listId") {
                                    type = NavType.IntType
                                })
                            ) {
                                val individualId = it.arguments!!.getInt("listId")
                                val individual =
                                    individualsList.first { it.individualRecordId == individualId }
                                IndividualScreen(individual, mapViewModel, favoritesViewModel)
                            }
                        }
                        NavBar(navBarViewModel)
                    }
                }
            }
        }
    }
}