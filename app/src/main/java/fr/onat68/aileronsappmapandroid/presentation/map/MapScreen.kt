@file:OptIn(ExperimentalComposeUiApi::class)

package fr.onat68.aileronsappmapandroid.presentation.map

import android.util.Log
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.Stable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.ui.viewinterop.NoOpUpdate
import com.mapbox.android.gestures.MoveGestureDetector
import com.mapbox.geojson.Point
import com.mapbox.maps.CameraOptions
import com.mapbox.maps.MapInitOptions
import com.mapbox.maps.MapView
import com.mapbox.maps.MapboxMap
import com.mapbox.maps.plugin.animation.flyTo
import com.mapbox.maps.plugin.annotation.annotations
import com.mapbox.maps.plugin.annotation.generated.CircleAnnotationManager
import com.mapbox.maps.plugin.annotation.generated.OnPointAnnotationClickListener
import com.mapbox.maps.plugin.annotation.generated.PointAnnotationManager
import com.mapbox.maps.plugin.annotation.generated.PolylineAnnotationManager
import com.mapbox.maps.plugin.annotation.generated.createCircleAnnotationManager
import com.mapbox.maps.plugin.annotation.generated.createPointAnnotationManager
import com.mapbox.maps.plugin.annotation.generated.createPolylineAnnotationManager
import com.mapbox.maps.plugin.gestures.OnMoveListener
import com.mapbox.maps.plugin.gestures.addOnMapClickListener
import com.mapbox.maps.plugin.gestures.addOnMoveListener
import com.mapbox.maps.plugin.gestures.removeOnMoveListener
import fr.onat68.aileronsappmapandroid.Constants
import fr.onat68.aileronsappmapandroid.Constants.MAP_STYLE
import fr.onat68.aileronsappmapandroid.presentation.navBar.NavBarItem
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.launch

@Composable
fun MapScreen(
    mapViewModel: MapViewModel,
    individualIdFilter: Int,
    gestureHandler: MapGestureHandler? = null,
    openIndividualSheet: ((NavBarItem, String) -> Unit)? = null,
) {
    val context = LocalContext.current

    val initialCameraOptions = CameraOptions.Builder()
        .center(Point.fromLngLat(42.12, 7.72))
        .zoom(15.5)
        .build()

    val mapInitOptions = MapInitOptions(
        context = context,
        cameraOptions = initialCameraOptions,
        textureView = true // temporary work-around as described here: https://github.com/mapbox/mapbox-maps-android/issues/1570
    )

    val mapView = remember { MapView(context, mapInitOptions) }

    val recordPoints = mapViewModel.recordPoints.collectAsState(initial = listOf())
    val recordPointsFiltered =
        if (individualIdFilter != 0) recordPoints.value.filter { it.individualId == individualIdFilter }
        else recordPoints.value

    var pointAnnotationManager: PointAnnotationManager? by remember {
        mutableStateOf(null)
    }

    var polylineAnnotationManager: PolylineAnnotationManager? by remember {
        mutableStateOf(null)
    }

    var circleAnnotationManager: CircleAnnotationManager? by remember {
        mutableStateOf(null)
    }

    gestureHandler?.let {
        MapGestureListener(mapboxMap = mapView.mapboxMap, gestureHandler = gestureHandler)
    }

    AndroidView(
        factory = {
            mapView.mapboxMap.loadStyle(MAP_STYLE)

            val annotationApi = mapView.annotations

            circleAnnotationManager = annotationApi.createCircleAnnotationManager()
            pointAnnotationManager = annotationApi.createPointAnnotationManager()
            polylineAnnotationManager = annotationApi.createPolylineAnnotationManager()

            mapView
        },
        update = {

            circleAnnotationManager?.let { circleAnnotationManager ->
                circleAnnotationManager.deleteAll()
                circleAnnotationManager.create(mapViewModel.generateListCircle(recordPointsFiltered))
            }

            pointAnnotationManager?.let { pointAnnotationManager ->
                pointAnnotationManager.deleteAll()
                pointAnnotationManager.create(mapViewModel.generateListPoint(recordPointsFiltered))


                if (openIndividualSheet != null) {
                    pointAnnotationManager.addClickListener(
                        OnPointAnnotationClickListener {
                            val individualId: String = it.getData().toString()
                            openIndividualSheet(
                                NavBarItem.Individuals,
                                "individualSheet/${individualId}"
                            )

                            false
                        })
                }
            }

            polylineAnnotationManager?.let { polylineAnnotationManager ->
                polylineAnnotationManager.deleteAll()
                polylineAnnotationManager.create(
                    mapViewModel.generateListPolyline(
                        recordPointsFiltered
                    )
                )
            }

            mapView.mapboxMap
                .flyTo(
                    CameraOptions.Builder()
                        .zoom(if (individualIdFilter == Constants.DEFAULT_FILTER) 6.0 else 7.0)
                        .center(mapViewModel.getCameraCenter(recordPointsFiltered))
                        .build()
                )

            NoOpUpdate
        },
        modifier = Modifier
            .fillMaxSize()
    )

    DisposableEffect(Unit) { // lifecycle is not properly managed and cause memory leak
        onDispose {
            mapView.onStop()
            mapView.onDestroy()
        }
    }
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

class MapGestureHandler {
    private val _gestureState = MutableStateFlow(false)
    val gestureState: StateFlow<Boolean> = _gestureState

    fun onGestureStarted() {
        _gestureState.value = true
    }

    fun onGestureEnded() {
        _gestureState.value = false
    }
}