package fr.ailerons.map.presentation.map

import android.graphics.Bitmap
import androidx.appcompat.content.res.AppCompatResources
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.ui.viewinterop.NoOpUpdate
import androidx.core.graphics.drawable.toBitmap
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.mapbox.android.gestures.MoveGestureDetector
import com.mapbox.geojson.Point
import com.mapbox.maps.CameraOptions
import com.mapbox.maps.MapInitOptions
import com.mapbox.maps.MapView
import com.mapbox.maps.MapboxMap
import com.mapbox.maps.plugin.animation.flyTo
import com.mapbox.maps.plugin.annotation.annotations
import com.mapbox.maps.plugin.annotation.generated.CircleAnnotationManager
import com.mapbox.maps.plugin.annotation.generated.PointAnnotation
import com.mapbox.maps.plugin.annotation.generated.PointAnnotationManager
import com.mapbox.maps.plugin.annotation.generated.PolylineAnnotationManager
import com.mapbox.maps.plugin.annotation.generated.createCircleAnnotationManager
import com.mapbox.maps.plugin.annotation.generated.createPointAnnotationManager
import com.mapbox.maps.plugin.annotation.generated.createPolylineAnnotationManager
import com.mapbox.maps.plugin.gestures.OnMoveListener
import com.mapbox.maps.plugin.gestures.addOnMoveListener
import com.mapbox.maps.plugin.gestures.removeOnMoveListener
import fr.ailerons.map.Constants
import fr.ailerons.map.data.entities.RecordPoint
import fr.ailerons.map.R

@Composable
fun MapScreen(
    viewModel: MapViewModel,
    individualIdFilter: Int? = null,
    gestureHandler: MapGestureHandler? = null,
    openIndividualSheet: ((Int) -> Unit)? = null,
) {
    LaunchedEffect(individualIdFilter) { viewModel.setIndividualIdFilter(individualIdFilter) }
    val recordPoints by viewModel.recordPoints.collectAsStateWithLifecycle(emptyList())

    AileronMap(
        recordPoints = recordPoints,
        gestureHandler = gestureHandler,
        onPointAnnotationClick = { annotation ->
            openIndividualSheet
                ?.let { openIndividualSheet(annotation.getData().toString().toInt()) }
            false
        }
    )
}

@Composable
fun AileronMap(
    recordPoints: List<RecordPoint>,
    gestureHandler: MapGestureHandler?,
    onPointAnnotationClick: (PointAnnotation) -> Boolean,
) {
    val context = LocalContext.current
    val marker = getMarker()


    val initialCameraOptions = CameraOptions.Builder()
        .center(Point.fromLngLat(42.12, 7.72))
        .zoom(15.5)
        .build()

    val mapInitOptions = MapInitOptions(
        context = context,
        cameraOptions = initialCameraOptions,
        textureView = true // temporary work-around as described here: https://github.com/mapbox/mapbox-maps-android/issues/1570
    )

    var pointAnnotationManager by remember { mutableStateOf<PointAnnotationManager?>(null) }

    var polylineAnnotationManager by remember { mutableStateOf<PolylineAnnotationManager?>(null) }

    var circleAnnotationManager by remember { mutableStateOf<CircleAnnotationManager?>(null) }

    val mapView = remember { MapView(context, mapInitOptions) }

    gestureHandler?.let {
        MapGestureListener(mapboxMap = mapView.mapboxMap, gestureHandler = gestureHandler)
    }

    AndroidView(
        factory = {
            mapView.mapboxMap.loadStyle(Constants.MAP_STYLE)

            val annotationApi = mapView.annotations

            circleAnnotationManager = annotationApi.createCircleAnnotationManager()
            pointAnnotationManager = annotationApi.createPointAnnotationManager()
            polylineAnnotationManager = annotationApi.createPolylineAnnotationManager()

            mapView
        },
        update = {
            circleAnnotationManager?.let { circleAnnotationManager ->
                circleAnnotationManager.deleteAll()
                circleAnnotationManager.create(recordPoints.toCircleAnnotationOptions())
            }

            pointAnnotationManager?.let { pointAnnotationManager ->
                pointAnnotationManager.deleteAll()
                pointAnnotationManager.create(recordPoints.toPointAnnotationOptions(marker))

                pointAnnotationManager.addClickListener { onPointAnnotationClick(it); false }
            }

            polylineAnnotationManager?.let { polylineAnnotationManager ->
                polylineAnnotationManager.deleteAll()
                polylineAnnotationManager.create(recordPoints.toPolylineAnnotationOptions())
            }

            mapView.mapboxMap
                .flyTo(
                    CameraOptions.Builder()
                        .zoom(7.0)
                        .center(getCameraCenter(recordPoints))
                        .build()
                )

            NoOpUpdate
        },
        modifier = Modifier
            .fillMaxSize()
    )
}

@Stable
@Composable
fun MapGestureListener(mapboxMap: MapboxMap, gestureHandler: MapGestureHandler) {
    val listener = object : OnMoveListener {
        override fun onMove(detector: MoveGestureDetector): Boolean {
            return false
        }

        override fun onMoveBegin(detector: MoveGestureDetector) {
            gestureHandler.onGestureStarted()
        }

        override fun onMoveEnd(detector: MoveGestureDetector) {
            gestureHandler.onGestureEnded()
        }
    }
    mapboxMap.addOnMoveListener(listener)

    DisposableEffect(Unit) {
        onDispose {
            mapboxMap.removeOnMoveListener(listener)
        }
    }
}

@Composable
fun rememberMapGestureHandler(): MapGestureHandler {
    return remember {
        MapGestureHandler()
    }
}

@Composable
fun getMarker(): Bitmap {
    val context = LocalContext.current
    return AppCompatResources.getDrawable(context, R.drawable.red_marker)!!.toBitmap()
}