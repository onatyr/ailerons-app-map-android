package fr.onat68.aileronsappmapandroid.map

import android.graphics.Bitmap
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
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
import fr.onat68.aileronsappmapandroid.RecordPoint
import kotlinx.coroutines.flow.Flow

class MapViewModel(
    val recordPoints: Flow<List<RecordPoint>>,
    private val navController: NavController,
    private val navBarViewModel: NavBarViewModel
): ViewModel() {

    private lateinit var recordPointsValue: List<RecordPoint>
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
    fun setRecordsData(mapRecordPoints: List<RecordPoint>, individualIdFilter: Int) {
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

    private fun generateCircle(circleAnnotationManager: CircleAnnotationManager?) {
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
                    navController.navigate("individualSheet/${individualId}")
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

    private fun changeNavBarToSpecies () {
        val speciesIndex = navBarViewModel.navBarItems.indexOfFirst { it.title == "Espèces" }
        navBarViewModel.switchNavBarItem(speciesIndex)
    }
}