package fr.onat68.aileronsappmapandroid

import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import fr.onat68.aileronsappmapandroid.map.Map
import fr.onat68.aileronsappmapandroid.species.Individual
import fr.onat68.aileronsappmapandroid.species.SpeciesScreen
import fr.onat68.aileronsappmapandroid.ui.theme.AileronsAppMapAndroidTheme
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.postgrest.Postgrest
import io.github.jan.supabase.postgrest.from
import kotlinx.coroutines.Dispatchers
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

                val navController = rememberNavController()
                var recordPoints by remember { mutableStateOf<List<RecordPoints>>(listOf()) }
                var individualsList by remember { mutableStateOf<List<Individual>>(listOf()) }

                LaunchedEffect(Unit) { // Fetch the data from Supabase
                    withContext(Dispatchers.IO) {

                        recordPoints = supabase.from("record")
                            .select().decodeList<RecordPoints>()

                        individualsList = supabase.from("individual")
                            .select().decodeList<Individual>()
                    }
                }

                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Column {
                        NavHost(
                            navController = navController,
                            startDestination = "map/${0}", // if changed don't forget to also change the route of map in NavBarItem
                            modifier = Modifier.weight(1f)
                        ) {
                            composable("favorites") { Text("Favorites") }
                            composable(
                                "map/{individualIdFilter}",
                                arguments = listOf(navArgument("individualIdFilter") {
                                    type = NavType.IntType
                                })
                            ) {
                                val individualIdFilter = it.arguments!!.getInt("individualIdFilter")
                                Map(recordPoints, individualIdFilter)

                            }
                            composable("species") { SpeciesScreen(individualsList, navController) }
                            composable("news") { Text("News") }
                        }
                        NavBar(navController)
                    }
                }
            }
        }
    }
}

@Composable
fun NavBar(navController: NavHostController) {
    var selectedItem by remember { mutableIntStateOf(1) }
    val items = listOf(NavBarItem.Favorites, NavBarItem.Map, NavBarItem.Species, NavBarItem.News)

    NavigationBar(Modifier.height(70.dp)) {

        items.forEachIndexed { index, item ->
            NavigationBarItem(
                selected = selectedItem == index,
                onClick = {
                    selectedItem = index
                    navController.navigate(item.route)
                },
                icon = {
                    Icon(
                        ImageVector.vectorResource(item.icon),
                        item.title,
                    )
                },
                label = { Text(item.title, fontSize = 12.sp) }
            )
        }
    }
}