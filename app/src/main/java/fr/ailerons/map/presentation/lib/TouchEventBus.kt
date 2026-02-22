package fr.ailerons.map.presentation.lib

import androidx.compose.ui.geometry.Offset
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow

class TouchEventBus {
    private val _position = MutableSharedFlow<Offset>(replay = 0, extraBufferCapacity = 64)
    val position = _position.asSharedFlow()

    fun tryEmit(offset: Offset) = _position.tryEmit(offset)
}