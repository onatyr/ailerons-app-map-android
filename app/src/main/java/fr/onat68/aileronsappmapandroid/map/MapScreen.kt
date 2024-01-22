package fr.onat68.aileronsappmapandroid.map

import androidx.compose.runtime.Composable
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
import com.mapbox.maps.MapView
import com.mapbox.maps.Style
import com.mapbox.maps.plugin.annotation.annotations
import com.mapbox.maps.plugin.annotation.generated.PointAnnotationManager
import com.mapbox.maps.plugin.annotation.generated.PointAnnotationOptions
import com.mapbox.maps.plugin.annotation.generated.PolylineAnnotationManager
import com.mapbox.maps.plugin.annotation.generated.PolylineAnnotationOptions
import com.mapbox.maps.plugin.annotation.generated.createPointAnnotationManager
import com.mapbox.maps.plugin.annotation.generated.createPolylineAnnotationManager
import fr.onat68.aileronsappmapandroid.R
import fr.onat68.aileronsappmapandroid.RecordPoints
import fr.onat68.aileronsappmapandroid.defaultFilter

//@OptIn(MapboxExperimental::class)
//@Composable
//fun Map(
//    recordPoints: List<RecordPoints>,
//    individualIdFilter: Int
//) { // To show all the points, individualIdFilter must be equal to 0
//
//    Log.d("ICI", "$individualIdFilter")
//    var points = recordPoints
//    if (individualIdFilter != 0) { // first try with -1 instead of 0 but some bugs can appear
//        points = points.filter { it.individualId == individualIdFilter }
//    }
//
//    val lines = points.groupBy { it.individualId }.values.map { records ->
//        records.map {
//            Point.fromLngLat(
//                it.longitude.toDouble(),
//                it.latitude.toDouble()
//            )
//        }
//    }
//
//    MapboxMap(
//        mapInitOptionsFactory = {
//            MapInitOptions(
//                context = it,
//                styleUri = Style.MAPBOX_STREETS,
//                cameraOptions = CameraOptions.Builder()
//                    .center(Point.fromLngLat(24.9384, 60.1699))
//                    .zoom(2.0)
//                    .build()
//            )
//        }) {
//        points.map { // Create marker annotations with the fetched data
//            PointAnnotation(
//                point = Point.fromLngLat(it.longitude.toDouble(), it.latitude.toDouble()),
//                iconImageBitmap = BitmapFactory.decodeResource(
//                    LocalContext.current.resources,
//                    R.drawable.red_marker
//                ),
//                iconSize = 0.3
//            )
//        }
//        lines.map { // Create polyline annotations with the fetched data
//            PolylineAnnotation(points = it, lineColorString = "#ee4e8b", lineWidth = 10.00)
//        }
//    }
//}

@Composable
fun Map(
    recordPoints: List<RecordPoints>,
    individualIdFilter: Int
) {

    var points = recordPoints
    if (individualIdFilter != defaultFilter) { // first try with -1 instead of 0 but some bugs can appear
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

    AndroidView(
        factory = {
            MapView(it).also { mapView ->
                mapView.mapboxMap.loadStyle(Style.MAPBOX_STREETS)
                val annotationApi = mapView.annotations
                pointAnnotationManager = annotationApi.createPointAnnotationManager()
                polylineAnnotationManager = annotationApi.createPolylineAnnotationManager()
            }
        },
        update = {

            pointAnnotationManager?.let {
                it.deleteAll()

                for (point in points){
                    val pointAnnotationOptions = PointAnnotationOptions()
                        .withPoint(Point.fromLngLat(point.longitude.toDouble(), point.latitude.toDouble()))
                        .withIconImage(marker)
                        .withIconSize(0.3)

                    it.create(pointAnnotationOptions)
                }
            }

            polylineAnnotationManager?.let {
                it.deleteAll()

                for (line in lines){
                    val polylineAnnotationOptions = PolylineAnnotationOptions()
                        .withPoints(line)
                        .withLineColor("#ee4e8b")
                        .withLineWidth(10.00)

                    it.create(polylineAnnotationOptions)
                }

            }

            NoOpUpdate
        },
        modifier = Modifier
    )
}