package fr.onat68.aileronsappmapandroid.map

import android.graphics.Bitmap
import androidx.lifecycle.ViewModel
import com.google.gson.GsonBuilder
import com.mapbox.geojson.Point
import com.mapbox.maps.plugin.annotation.generated.CircleAnnotationManager
import com.mapbox.maps.plugin.annotation.generated.CircleAnnotationOptions
import com.mapbox.maps.plugin.annotation.generated.OnPointAnnotationClickListener
import com.mapbox.maps.plugin.annotation.generated.PointAnnotationManager
import com.mapbox.maps.plugin.annotation.generated.PointAnnotationOptions
import com.mapbox.maps.plugin.annotation.generated.PolylineAnnotationManager
import com.mapbox.maps.plugin.annotation.generated.PolylineAnnotationOptions
import fr.onat68.aileronsappmapandroid.Constants
import fr.onat68.aileronsappmapandroid.NavBarViewModel
import fr.onat68.aileronsappmapandroid.data.entities.RecordPoint
import fr.onat68.aileronsappmapandroid.data.entities.RecordPointDTO
import fr.onat68.aileronsappmapandroid.data.repositories.RecordPointRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class MapViewModel(
    private val recordPointRepository: RecordPointRepository,
    private val navBarViewModel: NavBarViewModel
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

    /**
     * Create the annotations on the map using the annotations manager
     */
    fun setDataAnnotations(
        circleAnnotationManager: CircleAnnotationManager?,
        pointAnnotationManager: PointAnnotationManager?,
        polylineAnnotationManager: PolylineAnnotationManager?
    ) {
        generateCircle(circleAnnotationManager)
        generatePoint(pointAnnotationManager)
        generatePolyline(polylineAnnotationManager)
    }

    fun getCameraCenter(): Point {
        return if (points.isNotEmpty()) centroid() else MapValues.defaultCamera
    }

    /**
     * return a Point that is the cendroid of all the points from the data fetched
     */

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
}

private fun generatePoint(pointAnnotationManager: PointAnnotationManager?) {

    pointAnnotationManager?.let {
        it.deleteAll()

        for (recordPoint in recordPointsValue.groupBy { record -> record.individualId }
            .map { mapPoint -> // Take the last point in the date ordered list to pin a point
                mapPoint.value.last()
            }) {
            val point = Point.fromLngLat(
                recordPoint.longitude.toDouble(),
                recordPoint.latitude.toDouble()
            )
            val pointAnnotationOptions = PointAnnotationOptions()
                .withPoint(point)
                .withIconImage(marker)
                .withIconSize(MapValues.pointIconSize)
                .withData(GsonBuilder().create().toJsonTree(recordPoint.individualId))

            it.create(pointAnnotationOptions)
        }
    }

    pointAnnotationManager?.apply {
        addClickListener(
            OnPointAnnotationClickListener {
                val individualId: String = it.getData().toString()
                changeNavBarToSpecies()
                navBarViewModel.navController.navigate("individualSheet/${individualId}")
                false
            }
        )
    }
}

private fun generatePolyline(polylineAnnotationManager: PolylineAnnotationManager?) {
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

private fun changeNavBarToSpecies() {
    val speciesIndex = navBarViewModel.navBarItems.indexOfFirst { it.title == "Espèces" }
    navBarViewModel.switchNavBarItem(speciesIndex)
}
}