package fr.ailerons.map.presentation.screens.map

import android.animation.Animator
import android.graphics.Bitmap
import androidx.appcompat.content.res.AppCompatResources
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.mapSaver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.ui.viewinterop.NoOpUpdate
import androidx.core.graphics.drawable.toBitmap
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.mapbox.android.gestures.MoveGestureDetector
import com.mapbox.geojson.Point
import com.mapbox.maps.CameraOptions
import com.mapbox.maps.CameraState
import com.mapbox.maps.EdgeInsets
import com.mapbox.maps.MapInitOptions
import com.mapbox.maps.MapView
import com.mapbox.maps.MapboxMap
import com.mapbox.maps.dsl.cameraOptions
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
import fr.ailerons.map.logger

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MapScreen(
    viewModel: MapViewModel = hiltViewModel(),
    individualIdFilter: Int? = null,
    gestureHandler: MapGestureHandler? = null,
    openIndividualSheet: ((Int) -> Unit)? = null,
    mapState: MapState = rememberMapState()
) {
    val bottomSheetState = rememberModalBottomSheetState()
    LaunchedEffect(individualIdFilter) { viewModel.setIndividualIdFilter(individualIdFilter) }
    val recordPoints by viewModel.recordPoints.collectAsStateWithLifecycle(emptyList())

    AileronMap(
        recordPoints = recordPoints,
        gestureHandler = gestureHandler,
        onPointAnnotationClick = { annotation ->
            openIndividualSheet
                ?.let { openIndividualSheet(annotation.getData().toString().toInt()) }
            false
        },
        mapState = mapState
    )

//    ModalBottomSheet(onDismissRequest = {}, sheetState = bottomSheetState) {
//        Text("Coucou")
//    }
}

@Composable
fun AileronMap(
    recordPoints: List<RecordPoint>,
    gestureHandler: MapGestureHandler?,
    onPointAnnotationClick: (PointAnnotation) -> Boolean,
    mapState: MapState,
) {
    val context = LocalContext.current
    val marker = getMarker()

    val mapInitOptions = MapInitOptions(
        context = context,
        cameraOptions = mapState.cameraOptions,
        textureView = true // temporary work-around as described here: https://github.com/mapbox/mapbox-maps-android/issues/1570
    )

    var pointAnnotationManager by remember { mutableStateOf<PointAnnotationManager?>(null) }

    var polylineAnnotationManager by remember { mutableStateOf<PolylineAnnotationManager?>(null) }

    var circleAnnotationManager by remember { mutableStateOf<CircleAnnotationManager?>(null) }

    val mapView = remember { MapView(context, mapInitOptions) }

    gestureHandler?.let {
        MapGestureListener(mapboxMap = mapView.mapboxMap, gestureHandler = gestureHandler)
    }

    ListenCameraStateChanges(mapView = mapView, mapState = mapState)

    LaunchedEffect(recordPoints.isNotEmpty(), mapState.isInit) {
        if (!mapState.isInit && recordPoints.isNotEmpty()) {
            mapView.mapboxMap.flyTo(
                cameraOptions = CameraOptions.Builder()
                    .zoom(7.0)
                    .center(getCameraCenter(recordPoints))
                    .build(),
                animatorListener = getMapInitListener(mapState)
            )
        }
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

data class MapState(
    var cameraOptions: CameraOptions,
    var isInit: Boolean
) {
    companion object {
        val Saver: Saver<MapState, *> = mapSaver(
            save = { mapState ->
                mapOf(
                    "longitude" to mapState.cameraOptions.center?.longitude(),
                    "latitude" to mapState.cameraOptions.center?.latitude(),
                    "zoom" to mapState.cameraOptions.zoom,
                    "bearing" to mapState.cameraOptions.bearing,
                    "pitch" to mapState.cameraOptions.pitch,
                    "isInit" to mapState.isInit
                )
            },
            restore = { map ->
                MapState(
                    cameraOptions = CameraOptions.Builder()
                        .center(
                            Point.fromLngLat(
                                map["longitude"] as? Double ?: 43.631538,
                                map["latitude"] as? Double ?: 3.860591
                            )
                        )
                        .zoom(map["zoom"] as Double)
                        .bearing(map["bearing"] as Double?)
                        .pitch(map["pitch"] as Double?)
                        .build(),
                    isInit = map["isInit"] as Boolean
                )
            }
        )
    }
}

fun getMapInitListener(mapState: MapState) = object : Animator.AnimatorListener {
    override fun onAnimationEnd(animation: Animator) {
        mapState.isInit = true
    }

    override fun onAnimationCancel(animation: Animator) {}
    override fun onAnimationRepeat(animation: Animator) {}
    override fun onAnimationStart(animation: Animator) {}
}


@Composable
fun rememberMapState(): MapState {
    return rememberSaveable(saver = MapState.Saver) {
        MapState(
            cameraOptions = CameraOptions.Builder()
                .center(Point.fromLngLat(43.631538, 3.860591))
                .zoom(20.0)
                .build(),
            isInit = false
        )
    }
}

@Composable
fun ListenCameraStateChanges(mapView: MapView, mapState: MapState) {
    DisposableEffect(mapView) {
        val subscription = mapView.mapboxMap.subscribeCameraChanged {
            it.cameraState.center

            mapState.cameraOptions = CameraOptions.Builder()
                .center(
                    Point.fromLngLat(
                        it.cameraState.center.longitude(),
                        it.cameraState.center.latitude()
                    )
                )
                .zoom(it.cameraState.zoom)
                .bearing(it.cameraState.bearing)
                .pitch(it.cameraState.pitch)
                .build()
        }

        onDispose {
            subscription.cancel()
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