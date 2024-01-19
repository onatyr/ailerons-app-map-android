package fr.onat68.aileronsappmapandroid

import android.content.Context
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
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
                            composable("map") { Map(supabase) }
                            composable("species") { Text("Species") }
                            composable("news") { Text("News") }
                        }
                        Box(modifier = Modifier.weight(0.11f)) {
                            NavBar(navController)
                        }
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

    NavigationBar(Modifier.fillMaxSize()) {

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
                        Modifier.size(40.dp)
                    )
                },
                label = { Text(item.title, fontSize = 8.sp) },
                modifier = Modifier.wrapContentHeight()
            )
        }
    }
}

@OptIn(MapboxExperimental::class)
@Composable
fun Map(supabase: SupabaseClient) {

    var records by remember { mutableStateOf<List<Record>>(listOf()) }
    var lineList by remember { mutableStateOf<List<List<Point>>>(listOf()) }

    LaunchedEffect(Unit) { // Fetch the data from Supabase
        withContext(Dispatchers.IO) {
            records = supabase.from("record")
                .select().decodeList<Record>()
            lineList = records.groupBy { it.individualId }.values.map { records ->
                records.map { Point.fromLngLat(it.longitude.toDouble(), it.latitude.toDouble()) }
            }
        }
    }
    MapboxMap(
        mapInitOptionsFactory = {
            MapInitOptions(
                context = it,
                styleUri = Style.MAPBOX_STREETS,
                cameraOptions = CameraOptions.Builder()
                    .center(Point.fromLngLat(24.9384, 60.1699))
                    .zoom(2.0)
                    .build()
            )
        }) {
        records.map { // Create marker annotations with the fetched data
            PointAnnotation(
                point = Point.fromLngLat(it.longitude.toDouble(), it.latitude.toDouble()),
                iconImageBitmap = BitmapFactory.decodeResource(
                    LocalContext.current.resources,
                    R.drawable.red_marker
                ),
                iconSize = 0.3
            )
        }
        lineList.map { // Create polyline annotations with the fetched data
            PolylineAnnotation(points = it, lineColorString = "#ee4e8b", lineWidth = 10.00)
        }
    }
}