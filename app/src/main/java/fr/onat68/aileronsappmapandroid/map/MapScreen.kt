package fr.onat68.aileronsappmapandroid.map

import android.graphics.BitmapFactory
import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import com.mapbox.geojson.Point
import com.mapbox.maps.CameraOptions
import com.mapbox.maps.MapInitOptions
import com.mapbox.maps.MapboxExperimental
import com.mapbox.maps.Style
import com.mapbox.maps.extension.compose.MapboxMap
import com.mapbox.maps.extension.compose.annotation.generated.PointAnnotation
import com.mapbox.maps.extension.compose.annotation.generated.PolylineAnnotation
import fr.onat68.aileronsappmapandroid.R
import fr.onat68.aileronsappmapandroid.RecordPoints

@OptIn(MapboxExperimental::class)
@Composable
fun Map(
    recordPoints: List<RecordPoints>,
    individualIdFilter: Int
) { // To show all the points, individualIdFilter must be equal to 0

    Log.d("ICI", "$individualIdFilter")
    var points = recordPoints
    if (individualIdFilter != 0) { // first try with -1 instead of 0 but some bugs can appear
        points = points.filter { it.individualId == individualIdFilter }
    }

    val lines = points.groupBy { it.individualId }.values.map { records ->
        records.map {
            Point.fromLngLat(
                it.longitude.toDouble(),
                it.latitude.toDouble()
            )
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
        points.map { // Create marker annotations with the fetched data
            PointAnnotation(
                point = Point.fromLngLat(it.longitude.toDouble(), it.latitude.toDouble()),
                iconImageBitmap = BitmapFactory.decodeResource(
                    LocalContext.current.resources,
                    R.drawable.red_marker
                ),
                iconSize = 0.3
            )
        }
        lines.map { // Create polyline annotations with the fetched data
            PolylineAnnotation(points = it, lineColorString = "#ee4e8b", lineWidth = 10.00)
        }
    }
}