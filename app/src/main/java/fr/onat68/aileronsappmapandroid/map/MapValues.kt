package fr.onat68.aileronsappmapandroid.map

import com.mapbox.geojson.Point

object MapValues {

    val defaultCamera: Point = Point.fromLngLat(48.0, 48.0)

    const val MAP_STYLE: String = "mapbox://styles/louiscoutel/clzfs0eqy00d201r35xf19d1o"

    const val CIRCLE_COLOR: String = "#ee4e8b"
    const val CIRCLE_RADIUS: Double = 2.0

    const val POINT_ICON_SIZE: Double = 0.3

    const val POLYLINE_COLOR: String = "#ee4e8b"
    const val POLYLINE_WIDTH: Double = 2.00
}
