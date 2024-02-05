package fr.onat68.aileronsappmapandroid.map

import android.graphics.Bitmap
import android.util.Log
import com.mapbox.geojson.Point
import com.mapbox.maps.plugin.annotation.generated.CircleAnnotationManager
import com.mapbox.maps.plugin.annotation.generated.CircleAnnotationOptions
import com.mapbox.maps.plugin.annotation.generated.OnPointAnnotationClickListener
import com.mapbox.maps.plugin.annotation.generated.PointAnnotationManager
import com.mapbox.maps.plugin.annotation.generated.PointAnnotationOptions
import com.mapbox.maps.plugin.annotation.generated.PolylineAnnotationManager
import com.mapbox.maps.plugin.annotation.generated.PolylineAnnotationOptions
import fr.onat68.aileronsappmapandroid.RecordPoint
import kotlinx.coroutines.flow.Flow

class MapViewModel(
    val recordPoints: Flow<List<RecordPoint>>,
) {
    fun generateCircle(circleAnnotationManager: CircleAnnotationManager?, points: List<Point>) {
        circleAnnotationManager?.let {
            it.deleteAll()

            for (point in points) {
                val circleAnnotationOptions = CircleAnnotationOptions()
                    .withPoint(point)
                    .withCircleRadius(MapValues.circleRadius)
                    .withCircleColor(MapValues.circleColor)

                it.create(circleAnnotationOptions)
            }
        }
    }

    fun generatePoint(pointAnnotationManager: PointAnnotationManager?, points: List<RecordPoint>, marker: Bitmap) {

        pointAnnotationManager?.let {
            it.deleteAll()

            for (point in points.groupBy { record -> record.individualId }
                .map { mapPoint ->
                    Point.fromLngLat(  // Take the last point in the date ordered list to pin a point
                        mapPoint.value.last().longitude.toDouble(),
                        mapPoint.value.last().latitude.toDouble()
                    )
                }) {
                val pointAnnotationOptions = PointAnnotationOptions()
                    .withPoint(point)
                    .withIconImage(marker)
                    .withIconSize(MapValues.pointIconSize)

                it.create(pointAnnotationOptions)
            }
        }

        pointAnnotationManager?.apply {
            addClickListener(
                OnPointAnnotationClickListener {
                    Log.d("heeey", "Ce point a été clické : ${it.point.longitude()}")

                    false
                }
            )
        }
    }

    fun generatePolyline(polylineAnnotationManager: PolylineAnnotationManager?, lines: List<List<Point>>) {
        polylineAnnotationManager?.let {
            it.deleteAll()

            for (line in lines) {
                val polylineAnnotationOptions = PolylineAnnotationOptions()
                    .withPoints(line)
                    .withLineColor(MapValues.polylineLineColor)
                    .withLineWidth(MapValues.polylineLineWidth)

                it.create(polylineAnnotationOptions)
            }
        }
    }
}