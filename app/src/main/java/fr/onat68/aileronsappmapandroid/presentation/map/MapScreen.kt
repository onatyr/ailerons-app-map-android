package fr.onat68.aileronsappmapandroid.presentation.map

import android.util.Log
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.ui.viewinterop.NoOpUpdate
import com.mapbox.geojson.Point
import com.mapbox.maps.CameraOptions
import com.mapbox.maps.MapInitOptions
import com.mapbox.maps.MapView
import com.mapbox.maps.plugin.animation.flyTo
import com.mapbox.maps.plugin.annotation.annotations
import com.mapbox.maps.plugin.annotation.generated.CircleAnnotationManager
import com.mapbox.maps.plugin.annotation.generated.OnPointAnnotationClickListener
import com.mapbox.maps.plugin.annotation.generated.PointAnnotationManager
import com.mapbox.maps.plugin.annotation.generated.PolylineAnnotationManager
import com.mapbox.maps.plugin.annotation.generated.createCircleAnnotationManager
import com.mapbox.maps.plugin.annotation.generated.createPointAnnotationManager
import com.mapbox.maps.plugin.annotation.generated.createPolylineAnnotationManager
import fr.onat68.aileronsappmapandroid.Constants
import fr.onat68.aileronsappmapandroid.Constants.MAP_STYLE
import fr.onat68.aileronsappmapandroid.Constants.TRANSPARENT_COLOR
import fr.onat68.aileronsappmapandroid.presentation.navBar.NavBarItem

@Composable
fun MapScreen(
    mapViewModel: MapViewModel,
    individualIdFilter: Int,
    openIndividualSheet: ((NavBarItem, String) -> Unit)? = null,
) {
    val context = LocalContext.current

    val initialCameraOptions = CameraOptions.Builder()
        .center(Point.fromLngLat(42.12, 7.72))
        .zoom(15.5)
        .build()

    val mapInitOptions = MapInitOptions(
        context = context,
        cameraOptions = initialCameraOptions,
    )

    val mapView = remember { MapView(context, mapInitOptions) }

    val recordPoints = mapViewModel.recordPoints.collectAsState(initial = listOf())
    val recordPointsFiltered =
        if (individualIdFilter != 0) recordPoints.value.filter { it.individualId == individualIdFilter }
        else recordPoints.value

    var pointAnnotationManager: PointAnnotationManager? by remember {
        mutableStateOf(null)
    }

    var polylineAnnotationManager: PolylineAnnotationManager? by remember {
        mutableStateOf(null)
    }

    var circleAnnotationManager: CircleAnnotationManager? by remember {
        mutableStateOf(null)
    }

    AndroidView(
        factory = {
            mapView.mapboxMap.loadStyle(MAP_STYLE)

            val annotationApi = mapView.annotations

            circleAnnotationManager = annotationApi.createCircleAnnotationManager()
            pointAnnotationManager = annotationApi.createPointAnnotationManager()
            polylineAnnotationManager = annotationApi.createPolylineAnnotationManager()

            mapView
        },
        update = {

            circleAnnotationManager?.let { circleAnnotationManager ->
                circleAnnotationManager.deleteAll()
                circleAnnotationManager.create(mapViewModel.generateListCircle(recordPointsFiltered))
            }

            pointAnnotationManager?.let { pointAnnotationManager ->
                pointAnnotationManager.deleteAll()
                pointAnnotationManager.create(mapViewModel.generateListPoint(recordPointsFiltered))


                if (openIndividualSheet != null) {
                    pointAnnotationManager.addClickListener(
                        OnPointAnnotationClickListener {
                            val individualId: String = it.getData().toString()
                            openIndividualSheet(
                                NavBarItem.Individuals,
                                "individualSheet/${individualId}"
                            )

                            false
                        })
                }
            }

            polylineAnnotationManager?.let { polylineAnnotationManager ->
                polylineAnnotationManager.deleteAll()
                polylineAnnotationManager.create(
                    mapViewModel.generateListPolyline(
                        recordPointsFiltered
                    )
                )
            }

            mapView.mapboxMap
                .flyTo(
                    CameraOptions.Builder()
                        .zoom(if (individualIdFilter == Constants.DEFAULT_FILTER) 6.0 else 7.0)
                        .center(mapViewModel.getCameraCenter(recordPointsFiltered))
                        .build()
                )

            NoOpUpdate
        },
        modifier = Modifier.fillMaxSize()
    )
}