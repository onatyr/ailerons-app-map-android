package fr.ailerons.map.presentation.lib

import android.graphics.Bitmap
import androidx.annotation.DrawableRes
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Canvas
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.asAndroidBitmap
import androidx.compose.ui.graphics.drawscope.CanvasDrawScope
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.RenderVectorGroup
import androidx.compose.ui.graphics.vector.VectorConfig
import androidx.compose.ui.graphics.vector.VectorPainter
import androidx.compose.ui.graphics.vector.VectorProperty
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.LayoutDirection
import androidx.core.graphics.toColorInt

@Composable
fun dynamicPainter(
    @DrawableRes drawableRes: Int,
    dynamicPathMap: Map<String, String>,
): VectorPainter {
    val imageVector = ImageVector.vectorResource(id = drawableRes)

    return rememberVectorPainter(
        defaultWidth = imageVector.defaultWidth,
        defaultHeight = imageVector.defaultHeight,
        viewportWidth = imageVector.viewportWidth,
        viewportHeight = imageVector.viewportHeight,
        name = imageVector.name,
        tintColor = Color.Unspecified,
        tintBlendMode = BlendMode.SrcIn,
        autoMirror = false,
        content = { _, _ ->
            RenderVectorGroup(
                group = imageVector.root,
                configs = dynamicPathMap.mapValues { (_, colorString) ->
                    object : VectorConfig {
                        override fun <T> getOrDefault(
                            property: VectorProperty<T>,
                            defaultValue: T
                        ): T {
                            return when (property) {
                                is VectorProperty.Fill -> SolidColor(Color(colorString.toColorInt())) as T
                                else -> defaultValue
                            }
                        }
                    }
                }
            )
        })
}

fun painterToBitmap(
    painter: VectorPainter,
    width: Int,
    height: Int,
    density: Density
): Bitmap {
    val imageBitmap = ImageBitmap(width, height)
    val canvas = Canvas(imageBitmap)

    val scope = CanvasDrawScope()
    scope.draw(
        density = density,
        layoutDirection = LayoutDirection.Ltr,
        canvas = canvas,
        size = androidx.compose.ui.geometry.Size(width.toFloat(), height.toFloat())
    ) {
        with(painter) {
            draw(size = size)
        }
    }

    return imageBitmap.asAndroidBitmap()
}