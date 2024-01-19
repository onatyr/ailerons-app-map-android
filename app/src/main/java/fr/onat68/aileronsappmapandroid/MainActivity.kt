package fr.onat68.aileronsappmapandroid

import android.content.Context
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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


    @OptIn(MapboxExperimental::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val ai: ApplicationInfo = applicationContext.packageManager
            .getApplicationInfo(applicationContext.packageName, PackageManager.GET_META_DATA)
        val supabase = createSupabaseClient(
            supabaseUrl = ai.metaData["supabaseUrl"].toString(),
            supabaseKey = ai.metaData["supabaseKey"].toString(),
        ) {
            install(Postgrest)
        }


        val points = listOf(Point.fromLngLat(17.00, 58.00), Point.fromLngLat(16.00, 57.00))
        val lines = listOf(
            Point.fromLngLat(17.94, 59.25),
            Point.fromLngLat(18.18, 59.37),
            Point.fromLngLat(19.0, 59.0)
        )

        setContent {

            AileronsAppMapAndroidTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Column {
                        Map(this@MainActivity, supabase, Modifier.weight(1f))
                        Box(modifier = Modifier.weight(0.08f)) {
                            NavBar()
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun NavBar() {
    var selectedItem by remember { mutableIntStateOf(1) }
    val items = listOf(NavBarItem.Favorites, NavBarItem.Map, NavBarItem.Species, NavBarItem.News)

    NavigationBar(Modifier.fillMaxHeight()){
        items.forEachIndexed { index, item ->
            NavigationBarItem(
                selected = selectedItem == index,
                onClick = { selectedItem = index },
                icon = { Icon( ImageVector.vectorResource(item.icon), item.title ) },
                label = { Text(item.title, fontSize = 10.sp) },
                modifier = Modifier.fillMaxHeight()
            )
        }
    }
}

@OptIn(MapboxExperimental::class)
@Composable
fun Map(context: Context, supabase: SupabaseClient, modifier: Modifier) {

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
    MapboxMap(modifier = modifier,
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
                    context.resources,
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