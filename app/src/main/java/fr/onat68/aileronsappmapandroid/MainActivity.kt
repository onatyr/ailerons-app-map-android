package fr.onat68.aileronsappmapandroid

import android.content.Context
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.icons.Icons
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemColors
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.mapbox.geojson.Point
import com.mapbox.maps.CameraOptions
import com.mapbox.maps.MapInitOptions
import com.mapbox.maps.MapboxExperimental
import com.mapbox.maps.Style
import com.mapbox.maps.extension.compose.MapboxMap
import com.mapbox.maps.extension.compose.annotation.generated.PointAnnotation
import com.mapbox.maps.extension.compose.annotation.generated.PolylineAnnotation
import fr.onat68.aileronsappmapandroid.map.Map
import fr.onat68.aileronsappmapandroid.species.SpeciesScreen
import fr.onat68.aileronsappmapandroid.ui.theme.AileronsAppMapAndroidTheme
import io.github.jan.supabase.SupabaseClient
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
                var records by remember { mutableStateOf<List<Record>>(listOf()) }
                var lineList by remember { mutableStateOf<List<List<Point>>>(listOf()) }

                LaunchedEffect(Unit) { // Fetch the data from Supabase
                    withContext(Dispatchers.IO) {
                        records = supabase.from("record")
                            .select().decodeList<Record>()
                        lineList = records.groupBy { it.individualId }.values.map { records ->
                            records.map {
                                Point.fromLngLat(
                                    it.longitude.toDouble(),
                                    it.latitude.toDouble()
                                )
                            }
                        }
                    }
                }

                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Column {
                        NavHost(
                            navController = navController,
                            startDestination = "map",
                            modifier = Modifier.weight(1f)
                        ) {
                            composable("favorites") { Text("Favorites") }
                            composable("map") { Map(supabase, records, lineList) }
                            composable("species") { SpeciesScreen() }
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