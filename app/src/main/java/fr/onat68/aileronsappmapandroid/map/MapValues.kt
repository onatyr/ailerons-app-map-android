package fr.onat68.aileronsappmapandroid.map

import com.mapbox.geojson.Point
import com.mapbox.maps.Style

object MapValues {

    val defaultCamera: Point = Point.fromLngLat(48.0, 48.0)

    const val mapStyle: String = Style.MAPBOX_STREETS

    const val pointIconSize: Double = 0.3

    const val polylineLineColor: String = "#ee4e8b"
    const val polylineLineWidth: Double = 10.00
}
