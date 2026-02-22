package fr.ailerons.map.lib.extensions

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.layout.boundsInRoot
import androidx.compose.ui.layout.onGloballyPositioned
import fr.ailerons.map.presentation.lib.LocalTouchEventBus

fun Modifier.onClickOutside(block: () -> Unit) = composed {
    val touchEventBus = LocalTouchEventBus.current
    var componentBounds by remember { mutableStateOf<Rect?>(null) }

    touchEventBus.position.collectAsEffect { position ->
        if (componentBounds?.contains(position) == false) block()
    }

    onGloballyPositioned { layoutCoordinates -> componentBounds = layoutCoordinates.boundsInRoot() }
}