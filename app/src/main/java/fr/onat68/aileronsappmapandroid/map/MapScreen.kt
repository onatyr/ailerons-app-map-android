package fr.onat68.aileronsappmapandroid.map

import android.util.Log
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
import com.mapbox.maps.plugin.annotation.generated.CircleAnnotationOptions
import com.mapbox.maps.plugin.annotation.generated.OnPointAnnotationClickListener
import com.mapbox.maps.plugin.annotation.generated.PointAnnotation
import com.mapbox.maps.plugin.annotation.generated.PointAnnotationManager
import com.mapbox.maps.plugin.annotation.generated.PointAnnotationOptions
import com.mapbox.maps.plugin.annotation.generated.PolylineAnnotationManager
import com.mapbox.maps.plugin.annotation.generated.PolylineAnnotationOptions
import com.mapbox.maps.plugin.annotation.generated.createCircleAnnotationManager
import com.mapbox.maps.plugin.annotation.generated.createPointAnnotationManager
import com.mapbox.maps.plugin.annotation.generated.createPolylineAnnotationManager
import com.mapbox.maps.plugin.gestures.addOnMapClickListener
import fr.onat68.aileronsappmapandroid.Constants
import fr.onat68.aileronsappmapandroid.R
import fr.onat68.aileronsappmapandroid.RecordPoints

@Composable
fun Map(
    mapViewModel: MapViewModel,
    individualIdFilter: Int
) {
    val recordsPoints: State<List<RecordPoints>> =
        mapViewModel.recordPoints.collectAsState(initial = listOf())

    var recordPointsFiltered = recordsPoints.value
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
            Log.d("hey", "${it.recordTimestamp}")
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
                mapView.mapboxMap.addOnMapClickListener {

                    Log.d("hola", "${it.longitude()}")
                    it.longitude() < 100
                }
                val annotationApi = mapView.annotations
                pointAnnotationManager = annotationApi.createPointAnnotationManager()
                polylineAnnotationManager = annotationApi.createPolylineAnnotationManager()
                circleAnnotationManager = annotationApi.createCircleAnnotationManager()

            }
        },
        update = { mapView ->

            circleAnnotationManager?.let {
                it.deleteAll()

                for (point in points) {
                    val circleAnnotationOptions = CircleAnnotationOptions()
                        .withPoint(point)
                        .withCircleRadius(MapValues.circleRadius)
                        .withCircleColor(MapValues.circleColor)

                    it.create(circleAnnotationOptions)
                }
            }

            pointAnnotationManager?.let {
                it.deleteAll()

                for (point in recordPointsFiltered.groupBy { record -> record.individualId }
                    .map { mapPoint ->
                        Point.fromLngLat(  // Take the last point in the date ordered list to pin a point
                            mapPoint.value.last().longitude.toDouble(),
                            mapPoint.value.last().latitude.toDouble()
                        )
                    }) {
                    val pointAnnotationOptions = PointAnnotationOptions()
                        .withPoint(point)
                        .withIconImage(marker)
                        .withIconSize(MapValues.pointIconSize)

                    it.create(pointAnnotationOptions)
                }
//                pointAnnotationManager?.apply {
//
//                    addClickListener(
//                        OnPointAnnotationClickListener {
//                            Log.d("heeey", "Ce point a été clické : ${it.point.longitude()}")
////                    Toast.makeText(context, "id: ${it.id}", Toast.LENGTH_LONG).show()
//                            false
//                        }
//                    )
//                }
            }
            pointAnnotationManager?.addClickListener(object : OnPointAnnotationClickListener {
                override fun onAnnotationClick(annotation: PointAnnotation): Boolean {
                    Log.d("heeey", "Ce point a été clické : ${annotation.point.longitude()}")
//                    Toast.makeText(this@MainActivity, "Marker clicked", Toast.LENGTH_SHORT).show()
                    return true
                }
            })



            polylineAnnotationManager?.let {
                it.deleteAll()

                for (line in lines) {
                    val polylineAnnotationOptions = PolylineAnnotationOptions()
                        .withPoints(line)
                        .withLineColor(MapValues.polylineLineColor)
                        .withLineWidth(MapValues.polylineLineWidth)

                    it.create(polylineAnnotationOptions)
                }
            }
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