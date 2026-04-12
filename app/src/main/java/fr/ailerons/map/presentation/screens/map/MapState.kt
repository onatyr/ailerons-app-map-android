package fr.ailerons.map.presentation.screens.map

import android.animation.Animator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.mapSaver
import androidx.compose.runtime.saveable.rememberSaveable
import com.mapbox.geojson.Point
import com.mapbox.maps.CameraOptions
import com.mapbox.maps.MapView

data class MapState(
    var center: Point?,
    var zoom: Double,
    var bearing: Double,
    var pitch: Double,
    var isInit: Boolean,
) {
    val cameraOptions: CameraOptions
        get() = CameraOptions.Builder()
            .center(center)
            .zoom(zoom)
            .bearing(bearing)
            .pitch(pitch)
            .build()

    companion object {
        val Saver: Saver<MapState, *> = mapSaver(
            save = { mapState ->
                mapOf(
                    "longitude" to mapState.center?.longitude(),
                    "latitude" to mapState.center?.latitude(),
                    "zoom" to mapState.zoom,
                    "bearing" to mapState.bearing,
                    "pitch" to mapState.pitch,
                    "isInit" to mapState.isInit
                )
            },
            restore = { map ->
                MapState(
                    center = Point.fromLngLat(
                        map["longitude"] as Double? ?: 43.631538,
                        map["latitude"] as Double? ?: 3.860591
                    ),
                    zoom = map["zoom"] as Double,
                    bearing = map["bearing"] as Double,
                    pitch = map["pitch"] as Double,
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
            center = Point.fromLngLat(43.631538, 3.860591),
            zoom = 20.0,
            bearing = 0.0,
            pitch = 0.0,
            isInit = false
        )
    }
}

@Composable
fun ListenCameraStateChanges(mapView: MapView, mapState: MapState) {
    DisposableEffect(mapView) {

        val subscription = mapView.mapboxMap.subscribeCameraChanged {
            val state = it.cameraState
            mapState.center = state.center
            mapState.zoom = state.zoom
            mapState.bearing = state.bearing
            mapState.pitch = state.pitch
        }

        onDispose {
            subscription.cancel()
        }
    }
}