package fr.onat68.aileronsappmapandroid.map

import android.util.Log
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.ui.viewinterop.NoOpUpdate
import androidx.core.graphics.drawable.toBitmap
import com.mapbox.maps.CameraOptions
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
import fr.onat68.aileronsappmapandroid.NavBarItem
import fr.onat68.aileronsappmapandroid.R
import fr.onat68.aileronsappmapandroid.data.entities.RecordPoint
import fr.onat68.aileronsappmapandroid.data.entities.RecordPointDTO

@Composable
fun Map(
    mapViewModel: MapViewModel,
    individualIdFilter: Int,
    openIndividualSheet: ((NavBarItem, String) -> Unit)? = null,
) {

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
            MapView(it).also { mapView ->
                mapView.mapboxMap.loadStyle(MapValues.mapStyle)

                val annotationApi = mapView.annotations

                circleAnnotationManager = annotationApi.createCircleAnnotationManager()
                pointAnnotationManager = annotationApi.createPointAnnotationManager()
                polylineAnnotationManager = annotationApi.createPolylineAnnotationManager()
            }
        },
        update = { mapView ->

            circleAnnotationManager.let { circleAnnotationManager ->
                circleAnnotationManager?.deleteAll()
                mapViewModel.generateListCircle(recordPointsFiltered)
                    .forEach { circleAnnotationManager?.create(it) }
            }

            pointAnnotationManager.let { pointAnnotationManager ->
                pointAnnotationManager?.deleteAll()
                mapViewModel.generateListPoint(recordPointsFiltered)
                    .forEach {
                        pointAnnotationManager?.create(it)
                    }

                if (openIndividualSheet != null) {
                    pointAnnotationManager?.addClickListener(
                        OnPointAnnotationClickListener {
                            val individualId: String = it.getData().toString()
                            openIndividualSheet(
                                NavBarItem.Species,
                                "individualSheet/${individualId}"
                            )

                            false
                        })
                }
            }

            polylineAnnotationManager.let { polylineAnnotationManager ->
                polylineAnnotationManager?.deleteAll()
                mapViewModel.generateListPolyline(recordPointsFiltered)
                    .forEach { polylineAnnotationManager?.create(it) }
            }

            val zoom =
                if (individualIdFilter == Constants.defaultFilter) 1.5 else 4.0 // Set the zoom closer if one individual is selected

            mapView.mapboxMap
                .flyTo(
                    CameraOptions.Builder().zoom(zoom)
                        .center(mapViewModel.getCameraCenter(recordPointsFiltered))
                        .build()
                )

            NoOpUpdate
        },
        modifier = Modifier.fillMaxSize()
    )
}