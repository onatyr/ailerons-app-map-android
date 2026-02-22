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