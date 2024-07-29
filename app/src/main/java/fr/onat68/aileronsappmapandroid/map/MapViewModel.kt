package fr.onat68.aileronsappmapandroid.map

import android.graphics.Bitmap
import androidx.lifecycle.ViewModel
import com.google.gson.GsonBuilder
import com.mapbox.geojson.Point
import com.mapbox.maps.plugin.annotation.generated.CircleAnnotationOptions
import com.mapbox.maps.plugin.annotation.generated.PointAnnotationOptions
import com.mapbox.maps.plugin.annotation.generated.PolylineAnnotationOptions
import dagger.hilt.android.lifecycle.HiltViewModel
import fr.onat68.aileronsappmapandroid.data.entities.RecordPoint
import fr.onat68.aileronsappmapandroid.data.repositories.RecordPointRepository
import javax.inject.Inject

@HiltViewModel
class MapViewModel @Inject constructor(
    private val recordPointRepository: RecordPointRepository,
    private val marker: Bitmap
) : ViewModel() {

    val recordPoints = recordPointRepository.getListRecordPoint()

    fun getCameraCenter(recordPoints: List<RecordPoint>): Point {
        return if (recordPoints.isNotEmpty()) centroid(recordPoints.map {
            Point.fromLngLat(
                it.longitude.toDouble(),
                it.latitude.toDouble()
            )
        }) else MapValues.defaultCamera
    }

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


    fun generateListCircle(recordPoints: List<RecordPoint>): MutableList<CircleAnnotationOptions> {
        val circleList = mutableListOf<CircleAnnotationOptions>()
        for (recordPoint in recordPoints) {
            val point = Point.fromLngLat(
                recordPoint.longitude.toDouble(),
                recordPoint.latitude.toDouble()
            )

            circleList.add(
                CircleAnnotationOptions()
                    .withPoint(point)
                    .withCircleRadius(MapValues.CIRCLE_RADIUS)
                    .withCircleColor(MapValues.CIRCLE_COLOR)
            )
        }
        return circleList
    }

    fun generateListPoint(recordPoints: List<RecordPoint>): MutableList<PointAnnotationOptions> {
        val pointList = mutableListOf<PointAnnotationOptions>()
        val recordPointGrouped = recordPoints.groupBy { record -> record.individualId }

        for (recordPointList in recordPointGrouped) {
            val point = Point.fromLngLat(
                recordPointList.value.last().longitude.toDouble(),
                recordPointList.value.last().latitude.toDouble()
            )
            pointList.add(
                PointAnnotationOptions()
                    .withPoint(point)
                    .withIconImage(marker)
                    .withIconSize(MapValues.POINT_ICON_SIZE)
                    .withData(
                        GsonBuilder().create().toJsonTree(recordPointList.value.last().individualId)
                    )
            )
        }
        return pointList
    }

    fun generateListPolyline(recordPoints: List<RecordPoint>): MutableList<PolylineAnnotationOptions> {
        val linesList = mutableListOf<PolylineAnnotationOptions>()
        val lines = recordPoints.groupBy { it.individualId }.values.map { records ->
            records.map {
                Point.fromLngLat(
                    it.longitude.toDouble(),
                    it.latitude.toDouble()
                )
            }
        }
        for (line in lines) {
            linesList.add(
                PolylineAnnotationOptions()
                    .withPoints(line)
                    .withLineColor(MapValues.POLYLINE_COLOR)
                    .withLineWidth(MapValues.POLYLINE_WIDTH)
            )
        }
        return linesList
    }

}