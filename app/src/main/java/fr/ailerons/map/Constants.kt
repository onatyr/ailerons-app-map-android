package fr.ailerons.map

import androidx.compose.ui.graphics.Color
import com.mapbox.geojson.Point

object Constants {
    const val DEFAULT_FILTER = 0

    val ORANGE_COLOR = Color(0xFFf19E37)

    val defaultCamera: Point = Point.fromLngLat(48.0, 48.0)

    const val MAPBOX_USERNAME = "louiscoutel"
    const val STYLE_ID = "clzfs0eqy00d201r35xf19d1o"
    const val MAP_STYLE: String = "mapbox://styles/$MAPBOX_USERNAME/$STYLE_ID"

    const val CIRCLE_RADIUS: Double = 2.0

    const val POINT_ICON_SIZE: Double = 0.3

    const val POLYLINE_WIDTH: Double = 2.00
}