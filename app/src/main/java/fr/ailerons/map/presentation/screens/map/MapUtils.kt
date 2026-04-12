package fr.ailerons.map.presentation.screens.map

import android.graphics.Bitmap
import com.google.gson.GsonBuilder
import com.mapbox.geojson.Point
import com.mapbox.maps.extension.style.layers.properties.generated.IconAnchor
import com.mapbox.maps.plugin.annotation.generated.CircleAnnotationOptions
import com.mapbox.maps.plugin.annotation.generated.PointAnnotationOptions
import com.mapbox.maps.plugin.annotation.generated.PolylineAnnotationOptions
import fr.ailerons.map.Constants
import fr.ailerons.map.data.entities.RecordPoint
import fr.ailerons.map.data.entities.RecordPointWithColor

fun RecordPoint.toPoint() = Point.fromLngLat(longitude.toDouble(), latitude.toDouble())!!

fun List<RecordPointWithColor>.toCircleAnnotationOptions() = map { recordPoint ->
    CircleAnnotationOptions()
        .withPoint(recordPoint.recordPoint.toPoint())
        .withCircleRadius(Constants.CIRCLE_RADIUS)
        .withCircleColor(recordPoint.color)
}

fun List<RecordPointWithColor>.toPointAnnotationOptions(getMarker: (colorString: String) -> Bitmap?) =
    groupBy { it.recordPoint.idIndividual }.values.map { records ->
        val record = records.last()
        PointAnnotationOptions()
            .withPoint(record.recordPoint.toPoint())
            .apply {
                getMarker(record.color)?.let {
                    withIconImage(it)
                        .withIconAnchor(IconAnchor.BOTTOM)
                        .withIconOffset(listOf(0.0, 10.0))
                }
            }
            .withIconSize(Constants.POINT_ICON_SIZE)
            .withData(
                GsonBuilder().create()
                    .toJsonTree(record.recordPoint.idIndividual to records.last().recordPoint.recordTimestamp)
            )

    }

fun List<RecordPointWithColor>.toPolylineAnnotationOptions() =
    groupBy { it.recordPoint.idIndividual }.values.map { records ->
        PolylineAnnotationOptions()
            .withPoints(records.map { it.recordPoint.toPoint() })
            .withLineColor(records.first().color)
            .withLineWidth(Constants.POLYLINE_WIDTH)
    }

fun centroid(points: List<Point>): Point {
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

