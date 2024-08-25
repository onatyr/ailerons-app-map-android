package fr.onat68.aileronsappmapandroid.presentation

import com.mapbox.geojson.Point
import fr.onat68.aileronsappmapandroid.Constants.MAPBOX_USERNAME
import fr.onat68.aileronsappmapandroid.Constants.STYLE_ID
import fr.onat68.aileronsappmapandroid.Constants.defaultCamera
import fr.onat68.aileronsappmapandroid.data.entities.RecordPoint

class MapboxUtils {
    companion object {
        private var recordPoints: List<RecordPoint> = emptyList()

        private val lon
            get() = getCameraCenter(recordPoints).longitude()
        private val lat
            get() = getCameraCenter(recordPoints).latitude()
        private val zoom = 7

        fun generateSnapshotUri(recordPoint: RecordPoint): String {
            return "https://api.mapbox.com/styles/v1" +
                    "/$MAPBOX_USERNAME/$STYLE_ID/static/{overlay}" +
                    "/$lon,$lat,$zoom" +
                    "/{width}x{height}{@2x}"
        }

        fun getCameraCenter(recordPoints: List<RecordPoint>): Point {
            return if (recordPoints.isNotEmpty()) centroid(recordPoints.map {
                Point.fromLngLat(
                    it.longitude.toDouble(),
                    it.latitude.toDouble()
                )
            }) else defaultCamera
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
    }


}