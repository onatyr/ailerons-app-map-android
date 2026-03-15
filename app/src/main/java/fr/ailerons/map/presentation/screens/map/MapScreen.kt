package fr.ailerons.map.presentation.screens.map

import android.view.Gravity
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.SheetValue
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.material3.rememberStandardBottomSheetState
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.ui.viewinterop.NoOpUpdate
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.mapbox.maps.CameraOptions
import com.mapbox.maps.MapInitOptions
import com.mapbox.maps.MapView
import com.mapbox.maps.plugin.animation.flyTo
import com.mapbox.maps.plugin.annotation.annotations
import com.mapbox.maps.plugin.annotation.generated.CircleAnnotationManager
import com.mapbox.maps.plugin.annotation.generated.PointAnnotation
import com.mapbox.maps.plugin.annotation.generated.PointAnnotationManager
import com.mapbox.maps.plugin.annotation.generated.PolylineAnnotationManager
import com.mapbox.maps.plugin.annotation.generated.createCircleAnnotationManager
import com.mapbox.maps.plugin.annotation.generated.createPointAnnotationManager
import com.mapbox.maps.plugin.annotation.generated.createPolylineAnnotationManager
import com.mapbox.maps.plugin.compass.compass
import fr.ailerons.map.Constants
import fr.ailerons.map.R
import fr.ailerons.map.data.entities.RecordPointWithColor
import fr.ailerons.map.presentation.lib.dynamicPainter
import fr.ailerons.map.presentation.lib.painterToBitmap
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MapScreen(
    viewModel: MapViewModel = hiltViewModel(),
    individualIdFilter: Int? = null,
    gestureHandler: MapGestureHandler? = null,
    mapState: MapState = rememberMapState(),
) {
    val scope = rememberCoroutineScope()

    val bottomSheetUiState by viewModel.bottomSheetUiState.collectAsStateWithLifecycle()

    val scaffoldState = rememberBottomSheetScaffoldState(
        bottomSheetState = rememberStandardBottomSheetState(
            initialValue = SheetValue.Hidden,
            skipHiddenState = false
        )
    )

    LaunchedEffect(individualIdFilter) { viewModel.setIdIndividualFilter(individualIdFilter) }

    val colors by viewModel.colors.collectAsStateWithLifecycle(emptyList())

    val recordPoints by viewModel.recordPoints.collectAsStateWithLifecycle(emptyList())

    MapBottomSheetScaffold(
        bottomSheetUiState = bottomSheetUiState,
        updateBottomSheetUiState = viewModel::updateBottomSheetUiState,
        scaffoldState = scaffoldState
    ) {
        Box {
            AileronMap(
                recordPoints = recordPoints,
                colors = colors,
                gestureHandler = gestureHandler,
                onPointAnnotationClick = { annotation ->
                    val jsonObject = annotation.getData()?.asJsonObject
                    val idIndividual = jsonObject?.get("first").toString().toIntOrNull()
                    val timestamp = jsonObject?.get("second")?.toString()
                    if (timestamp != null && idIndividual != null) {
                        viewModel.updateBottomSheetUiState(
                            params = BottomSheetParams.IndividualPointAnnotation(
                                idIndividual = idIndividual,
                                timestamp = timestamp
                            )
                        )
                    }
                    false
                },
                mapState = mapState
            )

            FilterIcon(
                modifier = Modifier.align(Alignment.TopEnd),
                onClick = {
                    viewModel.updateBottomSheetUiState(
                        params =
                            if (bottomSheetUiState is BottomSheetUiState.IndividualFilters)
                                null.also { scope.launch { scaffoldState.bottomSheetState.hide() } }
                            else BottomSheetParams.IndividualFilters
                    )
                })
        }
    }
}

@Composable
fun AileronMap(
    recordPoints: List<RecordPointWithColor>,
    colors: List<String>,
    gestureHandler: MapGestureHandler?,
    onPointAnnotationClick: (PointAnnotation) -> Boolean,
    mapState: MapState,
) {
    val context = LocalContext.current

    val mapInitOptions = MapInitOptions(
        context = context,
        cameraOptions = mapState.cameraOptions,
        textureView = true // temporary work-around as described here: https://github.com/mapbox/mapbox-maps-android/issues/1570
    )

    val density = LocalDensity.current
    val markers =
        colors.associateWith { stringColor ->
            painterToBitmap(
                dynamicPainter(
                    drawableRes = R.drawable.ic_ray_marker,
                    dynamicPathMap = mapOf("dynamic_path" to stringColor)
                ),
                width = 250,
                height = 320,
                density
            )
        }


    var pointAnnotationManager by remember { mutableStateOf<PointAnnotationManager?>(null) }

    var polylineAnnotationManager by remember {
        mutableStateOf<PolylineAnnotationManager?>(
            null
        )
    }

    var circleAnnotationManager by remember { mutableStateOf<CircleAnnotationManager?>(null) }

    val mapView = remember { MapView(context, mapInitOptions) }

    gestureHandler?.let {
        MapGestureListener(mapboxMap = mapView.mapboxMap, gestureHandler = gestureHandler)
    }

    ListenCameraStateChanges(mapView = mapView, mapState = mapState)

    LaunchedEffect(recordPoints.isNotEmpty(), mapState.isInit) {
        if (!mapState.isInit && recordPoints.isNotEmpty()) {
            mapView.mapboxMap.flyTo(
                cameraOptions = CameraOptions.Builder()
                    .zoom(7.0)
                    .center(
                        if (recordPoints.isNotEmpty()) centroid(recordPoints.map { it.recordPoint.toPoint() })
                        else Constants.defaultCamera
                    )
                    .build(),
                animatorListener = getMapInitListener(mapState)
            )
        }
    }

    AndroidView(
        factory = {
            mapView.mapboxMap.loadStyle(Constants.MAP_STYLE)

            mapView.compass.updateSettings {
                enabled = true
                position = Gravity.TOP or Gravity.END
                marginTop = 180f
                marginRight = 16f
            }

            val annotationApi = mapView.annotations

            circleAnnotationManager = annotationApi.createCircleAnnotationManager()
            pointAnnotationManager = annotationApi.createPointAnnotationManager()
            polylineAnnotationManager = annotationApi.createPolylineAnnotationManager()

            mapView
        },
        update = {
            circleAnnotationManager?.let { circleAnnotationManager ->
                circleAnnotationManager.deleteAll()
                circleAnnotationManager.create(recordPoints.toCircleAnnotationOptions())
            }

            pointAnnotationManager?.let { pointAnnotationManager ->
                pointAnnotationManager.deleteAll()
                pointAnnotationManager.create(
                    recordPoints.toPointAnnotationOptions(
                        getMarker = { markers[it] }
                    )
                )

                pointAnnotationManager.addClickListener { onPointAnnotationClick(it); false }
            }

            polylineAnnotationManager?.let { polylineAnnotationManager ->
                polylineAnnotationManager.deleteAll()
                polylineAnnotationManager.create(recordPoints.toPolylineAnnotationOptions())
            }

            NoOpUpdate
        },
        modifier = Modifier
            .fillMaxSize()
    )
}


@Composable
fun FilterIcon(onClick: () -> Unit, modifier: Modifier = Modifier) {
    val cornerSize = 10.dp
    Box(
        modifier = modifier
            .padding(top = 12.dp, bottom = 10.dp, start = 10.dp, end = 10.dp)
            .size(40.dp)
            .background(color = Color.White, shape = RoundedCornerShape(cornerSize))
            .clip(RoundedCornerShape(cornerSize))
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = ripple(bounded = true),
                onClick = onClick
            )
            .padding(top = 8.5.dp, bottom = 8.dp),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            painter = painterResource(R.drawable.ic_filter),
            contentDescription = "Record filters",
            modifier = Modifier.size(19.dp)
        )
    }
}
