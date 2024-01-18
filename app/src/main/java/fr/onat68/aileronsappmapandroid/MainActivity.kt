package fr.onat68.aileronsappmapandroid

import android.R.style
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.DrawableRes
import androidx.appcompat.content.res.AppCompatResources
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import com.google.android.gms.maps.model.LatLng
import com.mapbox.geojson.Feature
import com.mapbox.geojson.FeatureCollection
import com.mapbox.geojson.LineString
import com.mapbox.geojson.Point
import com.mapbox.maps.CameraOptions
import com.mapbox.maps.MapView
import com.mapbox.maps.MapboxExperimental
import com.mapbox.maps.extension.style.layers.addLayer
import com.mapbox.maps.extension.style.layers.generated.LineLayer
import com.mapbox.maps.extension.style.layers.generated.lineLayer
import com.mapbox.maps.extension.style.layers.properties.generated.LineCap
import com.mapbox.maps.extension.style.layers.properties.generated.LineJoin
import com.mapbox.maps.extension.style.sources.addSource
import com.mapbox.maps.extension.style.sources.generated.GeoJsonSource
import com.mapbox.maps.extension.style.sources.generated.geoJsonSource
import com.mapbox.maps.extension.style.style
import com.mapbox.maps.extension.style.terrain.generated.terrain
import com.mapbox.maps.plugin.annotation.AnnotationConfig
import com.mapbox.maps.plugin.annotation.annotations
import com.mapbox.maps.plugin.annotation.generated.PointAnnotationManager
import com.mapbox.maps.plugin.annotation.generated.PointAnnotationOptions
import com.mapbox.maps.plugin.annotation.generated.PolylineAnnotationManager
import com.mapbox.maps.plugin.annotation.generated.PolylineAnnotationOptions
import com.mapbox.maps.plugin.annotation.generated.createPointAnnotationManager
import com.mapbox.maps.plugin.annotation.generated.createPolylineAnnotationManager
import fr.onat68.aileronsappmapandroid.ui.theme.AileronsAppMapAndroidTheme


class MainActivity : ComponentActivity() {
    private lateinit var mapView: MapView


    @OptIn(MapboxExperimental::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mapView = MapView(this)

        val annotationApi = mapView.annotations
        val pointAnnotationManager = annotationApi.createPointAnnotationManager()
        val polylineAnnotationManager = annotationApi.createPolylineAnnotationManager()

        mapView.mapboxMap.setCamera(
            CameraOptions.Builder()
                .center(Point.fromLngLat(-98.0, 39.5))
                .pitch(0.0)
                .zoom(2.0)
                .bearing(0.0)
                .build()
        )
        mapView.mapboxMap.loadStyle(
            style(style = "mapbox://styles/louiscoutel/clr50uilo01ex01qu43ur5x21")
            {

            }) {
        }

        val points = listOf(
            Point.fromLngLat(17.94, 59.25),
            Point.fromLngLat(18.18, 59.37),
            Point.fromLngLat(19.0, 59.0)
        )

        addPoint(this, pointAnnotationManager, 17.95, 30.00)
        addPolyline(polylineAnnotationManager, points, "#ee4e8b")

        setContent {

            AileronsAppMapAndroidTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    setContentView(mapView)
                }
            }
        }
    }
}


fun addPoint(
    context: Context,
    pointAnnotationManager: PointAnnotationManager,
    longitude: Double,
    latitude: Double
) {
    // Set options for the resulting symbol layer.
    val pointAnnotationOptions: PointAnnotationOptions = PointAnnotationOptions()
        // Define a geographic coordinate.
        .withPoint(Point.fromLngLat(longitude, latitude))
        // Specify the bitmap you assigned to the point annotation
        // The bitmap will be added to map style automatically.
        .withIconImage(BitmapFactory.decodeResource(context.resources, R.drawable.red_marker))
    // Add the resulting pointAnnotation to the map.
    pointAnnotationManager.create(pointAnnotationOptions)
}

fun addPolyline(
    polylineAnnotationManager: PolylineAnnotationManager,
    points: List<Point>,
    lineColor: String
) {

    // Set options for the resulting line layer.
    val polylineAnnotationOptions: PolylineAnnotationOptions = PolylineAnnotationOptions()
        .withPoints(points)
        // Style the line that will be added to the map.
        .withLineColor(lineColor)
        .withLineWidth(5.0)
    // Add the resulting line to the map.

    polylineAnnotationManager.create(polylineAnnotationOptions)
}