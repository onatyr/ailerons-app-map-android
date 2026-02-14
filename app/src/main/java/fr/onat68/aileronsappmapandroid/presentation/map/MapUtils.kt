package fr.onat68.aileronsappmapandroid.presentation.map

import android.graphics.Bitmap
import com.google.gson.GsonBuilder
import com.mapbox.geojson.Point
import com.mapbox.maps.plugin.annotation.generated.CircleAnnotationOptions
import com.mapbox.maps.plugin.annotation.generated.PointAnnotationOptions
import com.mapbox.maps.plugin.annotation.generated.PolylineAnnotationOptions
import fr.onat68.aileronsappmapandroid.Constants.CIRCLE_COLOR
import fr.onat68.aileronsappmapandroid.Constants.CIRCLE_RADIUS
import fr.onat68.aileronsappmapandroid.Constants.POINT_ICON_SIZE
import fr.onat68.aileronsappmapandroid.Constants.POLYLINE_COLOR
import fr.onat68.aileronsappmapandroid.Constants.POLYLINE_WIDTH
import fr.onat68.aileronsappmapandroid.Constants.defaultCamera
import fr.onat68.aileronsappmapandroid.data.entities.RecordPoint

fun RecordPoint.toPoint() = Point.fromLngLat(longitude.toDouble(), latitude.toDouble())!!

fun List<RecordPoint>.toCircleAnnotationOptions() = map { recordPoint ->
    CircleAnnotationOptions()
        .withPoint(recordPoint.toPoint())
        .withCircleRadius(CIRCLE_RADIUS)
        .withCircleColor(CIRCLE_COLOR)
}

fun List<RecordPoint>.toPointAnnotationOptions(marker: Bitmap) =
    groupBy { it.individualId }.values.map { records ->
        PointAnnotationOptions()
            .withPoint(records.last().toPoint())
            .withIconImage(marker)
            .withIconSize(POINT_ICON_SIZE)
            .withData(
                GsonBuilder().create()
                    .toJsonTree(records.last().individualId)
            )
    }

fun List<RecordPoint>.toPolylineAnnotationOptions() =
    groupBy { it.individualId }.values.map { records ->
        PolylineAnnotationOptions()
            .withPoints(records.map { it.toPoint() })
            .withLineColor(POLYLINE_COLOR)
            .withLineWidth(POLYLINE_WIDTH)
    }

fun getCameraCenter(recordPoints: List<RecordPoint>) =
    if (recordPoints.isNotEmpty()) centroid(recordPoints.map { it.toPoint() }) else defaultCamera

private fun centroid(points: List<Point>): Point {
    var longitude = 0.0
    var latitude = 0.0

    for (point in points) {
        longitude += point.longitude()
        latitude += point.latitude()
    }

    longitude /= points.size.toDouble()
    latitude /= points.size.toDouble()

    return Point.fromLngLat(longitude, latitude)
}

