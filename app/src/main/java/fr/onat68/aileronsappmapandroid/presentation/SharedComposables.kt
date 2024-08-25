package fr.onat68.aileronsappmapandroid.presentation

import android.util.Log
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectVerticalDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Velocity
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import fr.onat68.aileronsappmapandroid.Constants
import fr.onat68.aileronsappmapandroid.R
import fr.onat68.aileronsappmapandroid.presentation.map.MapGestureHandler
import fr.onat68.aileronsappmapandroid.presentation.map.rememberMapGestureHandler

@Composable
fun ScreenSurface(content: @Composable () -> Unit) {
    Box(modifier = Modifier
        .fillMaxSize()
        .background(Color.White)) {
        content()
    }
}

@Composable
fun ScrollableColumnWithHeader(
    headerLabel: String,
    modifier: Modifier = Modifier,
    scrollState: ScrollState = rememberScrollState(),
    mapGestureHandler: MapGestureHandler = rememberMapGestureHandler(),
    content: @Composable ColumnScope.() -> Unit
) {
    val isMapGestureInProgress = mapGestureHandler.gestureState.collectAsState()
    Log.e("TAG", "ScrollableColumnWithHeader: $isMapGestureInProgress", )
    Column(modifier = modifier) {
        Header(label = headerLabel)
        LazyColumn(userScrollEnabled = !isMapGestureInProgress.value) {
            item {
                content()
            }
        }
    }
}

@Composable
fun Header(label: String) {
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .height(IntrinsicSize.Min)
            .padding(horizontal = 10.dp, vertical = 4.dp)
    ) {
        Button(
            onClick = LocalPopBackStack.current,
            contentPadding = PaddingValues(0.dp),
            colors = ButtonColors( // TODO pass them in LocalProvider
                containerColor = Color.Transparent,
                contentColor = Color.Transparent,
                disabledContentColor = Color.Transparent,
                disabledContainerColor = Color.Transparent
            )
        ) {
            Icon(
                painterResource(R.drawable.left_arrow),
                contentDescription = "Go back button",
                tint = Constants.ORANGE_COLOR,
            )
        }
        Text(
            text = label,
            fontSize = 30.sp,
            fontFamily = LocalCustomFont.current,
        )
    }
}