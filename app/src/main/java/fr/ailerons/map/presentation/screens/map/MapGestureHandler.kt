package fr.ailerons.map.presentation.screens.map

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import com.mapbox.android.gestures.MoveGestureDetector
import com.mapbox.maps.MapboxMap
import com.mapbox.maps.plugin.gestures.OnMoveListener
import com.mapbox.maps.plugin.gestures.addOnMoveListener
import com.mapbox.maps.plugin.gestures.removeOnMoveListener
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

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

@Composable
fun rememberMapGestureHandler(): MapGestureHandler {
    return remember {
        MapGestureHandler()
    }
}
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