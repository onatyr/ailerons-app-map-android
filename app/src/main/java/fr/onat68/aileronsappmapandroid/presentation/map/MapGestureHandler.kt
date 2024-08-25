package fr.onat68.aileronsappmapandroid.presentation.map

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