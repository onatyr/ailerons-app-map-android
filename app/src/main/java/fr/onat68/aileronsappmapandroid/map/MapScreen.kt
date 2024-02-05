package fr.onat68.aileronsappmapandroid.map

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
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
import com.mapbox.maps.plugin.annotation.generated.PointAnnotationManager
import com.mapbox.maps.plugin.annotation.generated.PolylineAnnotationManager
import com.mapbox.maps.plugin.annotation.generated.createCircleAnnotationManager
import com.mapbox.maps.plugin.annotation.generated.createPointAnnotationManager
import com.mapbox.maps.plugin.annotation.generated.createPolylineAnnotationManager
import fr.onat68.aileronsappmapandroid.R
import fr.onat68.aileronsappmapandroid.RecordPoint

@Composable
fun Map(
    mapViewModel: MapViewModel,
    individualId: Int
) {

    val recordPoints: State<List<RecordPoint>> =
        mapViewModel.recordPoints.collectAsState(initial = listOf())

    val individualIdFilter by remember {
        mutableIntStateOf(individualId)
    }

    mapViewModel.setMarker(LocalContext.current.getDrawable(R.drawable.red_marker)!!.toBitmap())
    mapViewModel.setRecordsData(recordPoints.value, individualIdFilter)

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

            mapViewModel.setDataAnnotations(
                circleAnnotationManager,
                pointAnnotationManager,
                polylineAnnotationManager
            )

            val zoom =
                if (individualIdFilter == 0) 1.5 else 4.0 // Set the zoom closer if one individual is selected

            mapView.mapboxMap
                .flyTo(
                    CameraOptions.Builder().zoom(zoom).center(mapViewModel.getCameraCenter())
                        .build()
                )

            NoOpUpdate
        },
        modifier = Modifier.fillMaxSize()
    )
}