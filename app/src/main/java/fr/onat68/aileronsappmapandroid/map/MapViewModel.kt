package fr.onat68.aileronsappmapandroid.map

import android.graphics.Bitmap
import androidx.lifecycle.ViewModel
import com.google.gson.GsonBuilder
import com.mapbox.geojson.Point
import com.mapbox.maps.plugin.annotation.generated.CircleAnnotationManager
import com.mapbox.maps.plugin.annotation.generated.CircleAnnotationOptions
import com.mapbox.maps.plugin.annotation.generated.PointAnnotationManager
import com.mapbox.maps.plugin.annotation.generated.PointAnnotationOptions
import com.mapbox.maps.plugin.annotation.generated.PolylineAnnotationManager
import com.mapbox.maps.plugin.annotation.generated.PolylineAnnotationOptions
import fr.onat68.aileronsappmapandroid.Constants
import fr.onat68.aileronsappmapandroid.NavBarViewModel
import fr.onat68.aileronsappmapandroid.data.entities.RecordPoint
import fr.onat68.aileronsappmapandroid.data.entities.RecordPointDTO
import fr.onat68.aileronsappmapandroid.data.repositories.RecordPointRepository

class MapViewModel(
    private val recordPointRepository: RecordPointRepository,
//    private val navBarViewModel: NavBarViewModel
) : ViewModel() {

    val recordPoints = recordPointRepository.getListRecordPoint()

    private lateinit var recordPointsValue: List<RecordPointDTO>
    private lateinit var points: List<Point>
    private lateinit var lines: List<List<Point>>

    private lateinit var marker: Bitmap

    /**
     * Set a Bitmap marker that will be used to generate points with pointAnnotationManager
     */
    fun setMarker(mapMarker: Bitmap) {
        marker = mapMarker
    }

    /**
     * With the data fetched, create a list of lines and a list of points with the indicated individualIdFilter
     */
    fun setRecordsData(mapRecordPoints: List<RecordPointDTO>, individualIdFilter: Int) {
        recordPointsValue = mapRecordPoints

        if (individualIdFilter != Constants.defaultFilter) { // first try with -1 instead of 0 but some bugs can appear
            recordPointsValue = mapRecordPoints.filter { it.individualId == individualIdFilter }
        }

        lines = recordPointsValue.groupBy { it.individualId }.values.map { records ->
            records.map {
                Point.fromLngLat(
                    it.longitude.toDouble(),
                    it.latitude.toDouble()
                )
            }
        }

        points =
            recordPointsValue.map {
                Point.fromLngLat(
                    it.longitude.toDouble(),
                    it.latitude.toDouble()
                )
            }
    }

    fun getCameraCenter(): Point {
        return if (points.isNotEmpty()) centroid() else MapValues.defaultCamera
    }

    private fun centroid(): Point {
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


    fun generateListCircle(recordPoints: List<RecordPoint>): List<CircleAnnotationOptions> {
        val circleList = emptyList<CircleAnnotationOptions>()
        for (point in points) {
            circleList.plus(
                CircleAnnotationOptions()
                    .withPoint(point)
                    .withCircleRadius(MapValues.circleRadius)
                    .withCircleColor(MapValues.circleColor)
            )
        }
        return circleList
    }


    fun generateListPoint(recordPoints: List<RecordPoint>): List<PointAnnotationOptions> {
        val pointList = emptyList<PointAnnotationOptions>()
        val recordPointGrouped = recordPoints.groupBy { record -> record.individualId }

        for (recordPoint in recordPointGrouped) {
            val point = Point.fromLngLat(
                recordPoint.value.last().longitude.toDouble(),
                recordPoint.value.last().latitude.toDouble()
            )
            pointList.plus(
                PointAnnotationOptions()
                    .withPoint(point)
                    .withIconImage(marker)
                    .withIconSize(MapValues.pointIconSize)
                    .withData(
                        GsonBuilder().create().toJsonTree(recordPoint.value.last().individualId)
                    )
            )
        }
        return pointList
    }


    fun generateListPolyline(recordPoints: List<RecordPoint>): List<PolylineAnnotationOptions> {
        val linesList: List<PolylineAnnotationOptions> = emptyList()
        val lines = recordPoints.groupBy { it.individualId }.values.map { records ->
            records.map {
                Point.fromLngLat(
                    it.longitude.toDouble(),
                    it.latitude.toDouble()
                )
            }
        }
        for (line in lines) {
            linesList.plus(
                PolylineAnnotationOptions()
                    .withPoints(line)
                    .withLineColor(MapValues.polylineLineColor)
                    .withLineWidth(MapValues.polylineLineWidth)
            )
        }
        return linesList
    }


    fun changeNavBarToSpecies() {
//        val speciesIndex = navBarViewModel.navBarItems.indexOfFirst { it.title == "Espèces" }
//        navBarViewModel.switchNavBarItem(speciesIndex)
    }
}