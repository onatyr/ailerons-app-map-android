package fr.onat68.aileronsappmapandroid.map

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.ui.viewinterop.NoOpUpdate
import androidx.core.graphics.drawable.toBitmap
import com.mapbox.geojson.Point
import com.mapbox.maps.CameraOptions
import com.mapbox.maps.MapView
import com.mapbox.maps.plugin.animation.flyTo
import com.mapbox.maps.plugin.annotation.annotations
import com.mapbox.maps.plugin.annotation.generated.CircleAnnotationManager
import com.mapbox.maps.plugin.annotation.generated.PointAnnotationManager
import com.mapbox.maps.plugin.annotation.generated.PolylineAnnotationManager
import com.mapbox.maps.plugin.annotation.generated.createCircleAnnotationManager
import com.mapbox.maps.plugin.annotation.generated.createPointAnnotationManager
import com.mapbox.maps.plugin.annotation.generated.createPolylineAnnotationManager
import fr.onat68.aileronsappmapandroid.Constants
import fr.onat68.aileronsappmapandroid.R
import fr.onat68.aileronsappmapandroid.RecordPoint

@Composable
fun Map(
    mapViewModel: MapViewModel,
    individualIdFilter: Int
) {
    val recordPoints: State<List<RecordPoint>> =
        mapViewModel.recordPoints.collectAsState(initial = listOf())

    var recordPointsFiltered = recordPoints.value
    if (individualIdFilter != Constants.defaultFilter) { // first try with -1 instead of 0 but some bugs can appear
        recordPointsFiltered = recordPointsFiltered.filter { it.individualId == individualIdFilter }
    }

    val lines = recordPointsFiltered.groupBy { it.individualId }.values.map { records ->
        records.map {
            Point.fromLngLat(
                it.longitude.toDouble(),
                it.latitude.toDouble()
            )
        }
    }

    val points =
        recordPointsFiltered.map {
            Point.fromLngLat(
                it.longitude.toDouble(),
                it.latitude.toDouble()
            )
        }

    val context = LocalContext.current
    val marker = remember(context) {
        context.getDrawable(R.drawable.red_marker)!!.toBitmap()
    }
    var pointAnnotationManager: PointAnnotationManager? by remember {
        mutableStateOf(null)
    }

    var polylineAnnotationManager: PolylineAnnotationManager? by remember {
        mutableStateOf(null)
    }

    var circleAnnotationManager: CircleAnnotationManager? by remember {
        mutableStateOf(null)
    }

    AndroidView(
        factory = {
            MapView(it).also { mapView ->
                mapView.mapboxMap.loadStyle(MapValues.mapStyle)

                val annotationApi = mapView.annotations
                pointAnnotationManager = annotationApi.createPointAnnotationManager()
                polylineAnnotationManager = annotationApi.createPolylineAnnotationManager()
                circleAnnotationManager = annotationApi.createCircleAnnotationManager()

            }
        },
        update = { mapView ->

            mapViewModel.generateCircle(circleAnnotationManager, points)

            mapViewModel.generatePoint(pointAnnotationManager, recordPointsFiltered, marker)

            mapViewModel.generatePolyline(polylineAnnotationManager, lines)

            if (points.isNotEmpty()) {
                val zoom =
                    if (individualIdFilter == 0) 1.5 else 4.0 // Set the zoom closer if one individual is selected
                mapView.mapboxMap
                    .flyTo(CameraOptions.Builder().zoom(zoom).center(centroid(points)).build())
            } else {
                mapView.mapboxMap
                    .flyTo(
                        CameraOptions.Builder().zoom(1.5).center(MapValues.defaultCamera).build()
                    )
            }

            NoOpUpdate
        },
        modifier = Modifier.fillMaxSize()
    )
}

fun centroid(points: List<Point>): Point {
    var longitude = 0.0
    var latitude = 0.0

    for (point in points) {
        longitude += point.longitude()
        latitude += point.latitude()
    }
    longitude /= points.size.toDouble()
    latitude /= points.size.toDouble()

    return Point.fromLngLat(longitude, latitude)
}