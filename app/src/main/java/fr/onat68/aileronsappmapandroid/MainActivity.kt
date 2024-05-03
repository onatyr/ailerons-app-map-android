package fr.onat68.aileronsappmapandroid

import android.content.Context
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import fr.onat68.aileronsappmapandroid.data.AppDatabase
import fr.onat68.aileronsappmapandroid.favorites.FavoriteScreen
import fr.onat68.aileronsappmapandroid.favorites.IndividualViewModel
import fr.onat68.aileronsappmapandroid.data.repositories.IndividualRepository
import fr.onat68.aileronsappmapandroid.data.repositories.RecordPointRepository
import fr.onat68.aileronsappmapandroid.individual.IndividualScreen
import fr.onat68.aileronsappmapandroid.map.Map
import fr.onat68.aileronsappmapandroid.map.MapViewModel
import fr.onat68.aileronsappmapandroid.news.NewsScreen
import fr.onat68.aileronsappmapandroid.species.SpeciesScreen
import fr.onat68.aileronsappmapandroid.ui.theme.AileronsAppMapAndroidTheme
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.postgrest.Postgrest


@Suppress("DEPRECATION")
class MainActivity : ComponentActivity() {

    private val ai: ApplicationInfo = applicationContext.packageManager
        .getApplicationInfo(applicationContext.packageName, PackageManager.GET_META_DATA)
    private val supabaseClient = createSupabaseClient(
        supabaseUrl = ai.metaData["supabaseUrl"].toString(),
        supabaseKey = ai.metaData["supabaseKey"].toString(),
    ) {
        install(Postgrest)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val database = AppDatabase.getInstance(this as Context)
        val individualRepository = IndividualRepository(supabaseClient, database.individualDao())
        val recordPointRepository = RecordPointRepository(supabaseClient, database.recordPointDao())

        individualRepository.fetchListIndividual()

        val individualViewModel = IndividualViewModel(individualRepository)
        Log.d("individualsList", "${individualViewModel.individualsList}")



//        setContent {
//
//
//            AileronsAppMapAndroidTheme {
//
//                val navController = rememberNavController()
//
//                val navBarViewModel = NavBarViewModel(navController)
//
//                val mapViewModel = MapViewModel(recordPointRepository ,navBarViewModel)
//
//
//                Surface(
//                    modifier = Modifier.fillMaxSize(),
//                    color = MaterialTheme.colorScheme.background
//                ) {
//                    Column {
//                        NavHost(
//                            navController = navController,
//                            startDestination = "map/${Constants.defaultFilter}",
//                            modifier = Modifier.weight(1f)
//                        ) {
//                            composable("favorites") {
//                                FavoriteScreen(
//                                    individualViewModel,
//                                    navController
//                                )
//                            }
//                            composable(
//                                "map/{individualIdFilter}",
//                                arguments = listOf(navArgument("individualIdFilter") {
//                                    type = NavType.IntType
//                                })
//                            ) {
//                                val individualIdFilter =
//                                    it.arguments!!.getInt("individualIdFilter")
//                                Map(mapViewModel, individualIdFilter)
//
//                            }
//                            composable("species") {
//                                SpeciesScreen(
//                                    individualViewModel,
//                                    navController
//                                )
//                            }
//                            composable("news") { NewsScreen() }
//                            composable(
//                                "individualSheet/{listId}",
//                                arguments = listOf(navArgument("listId") {
//                                    type = NavType.IntType
//                                })
//                            ) { entry ->
//                                val individualId = entry.arguments!!.getInt("listId")
//                                val individual =
//                                    individualsList.first { it.id == individualId }
//                                IndividualScreen(individual, mapViewModel, individualViewModel)
//                            }
//                        }
//                        NavBar(navBarViewModel)
//                    }
//                }
//            }
//        }
    }
}