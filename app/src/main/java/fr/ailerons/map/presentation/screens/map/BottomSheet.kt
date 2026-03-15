package fr.ailerons.map.presentation.screens.map

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.BottomSheetScaffoldState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SheetState
import androidx.compose.material3.SheetValue
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.graphics.toColorInt
import fr.ailerons.map.R
import fr.ailerons.map.data.entities.Individual
import fr.ailerons.map.lib.extensions.onClickOutside
import fr.ailerons.map.presentation.lib.LocalCustomFont
import fr.ailerons.map.presentation.lib.dynamicPainter
import fr.ailerons.map.presentation.screens.individual.IndividualSheet
import fr.ailerons.map.presentation.screens.map.MapConst.BOTTOM_SHEET_DRAG_HANDLER_HEIGHT
import fr.ailerons.map.presentation.screens.map.MapConst.INDIVIDUAL_HEADER_HEIGHT
import fr.ailerons.map.presentation.screens.map.MapConst.INDIVIDUAL_HEADER_HORIZONTAL_PADDING
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MapBottomSheetScaffold(
    bottomSheetUiState: BottomSheetUiState?,
    updateBottomSheetUiState: (BottomSheetParams?) -> Unit,
    scaffoldState: BottomSheetScaffoldState,
    mapContent: @Composable () -> Unit
) {
    LaunchedEffect(bottomSheetUiState) { if (bottomSheetUiState != null) scaffoldState.bottomSheetState.partialExpand() }

    LaunchedEffect(scaffoldState.bottomSheetState.currentValue) {
        if (scaffoldState.bottomSheetState.currentValue == SheetValue.Hidden)
            updateBottomSheetUiState(null)
    }

    BottomSheetScaffold(
        scaffoldState = scaffoldState,
        sheetContent = {
            bottomSheetUiState?.let {
                BottomSheetContent(
                    uiState = it,
                    sheetState = scaffoldState.bottomSheetState
                )
            }
        },
        sheetShape = RoundedCornerShape(
            topStart = 10.dp,
            topEnd = 10.dp
        ),
        sheetDragHandle = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null,
                        onClick = {}
                    )
                    .height(BOTTOM_SHEET_DRAG_HANDLER_HEIGHT),
                contentAlignment = Alignment.Center
            ) {
                Box(
                    modifier = Modifier
                        .width(45.dp)
                        .height(3.5.dp)
                        .background(
                            color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.35f),
                            shape = RoundedCornerShape(3.dp)
                        )
                )
            }
        },
        sheetPeekHeight =
            INDIVIDUAL_HEADER_HEIGHT
                    + BOTTOM_SHEET_DRAG_HANDLER_HEIGHT
                    + INDIVIDUAL_HEADER_HORIZONTAL_PADDING,
        sheetSwipeEnabled = true,
        sheetMaxWidth = Dp.Infinity
    ) {
        mapContent()
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BottomSheetContent(uiState: BottomSheetUiState, sheetState: SheetState) {
    val scope = rememberCoroutineScope()
    Box(
        Modifier
            .background(
                color = Color("#F0F0F0".toColorInt()),
                shape = RectangleShape
            )
            .onClickOutside {
                if (sheetState.currentValue == SheetValue.Expanded)
                    scope.launch { sheetState.partialExpand() }
            }
    ) {
        when (uiState) {
            is BottomSheetUiState.IndividualFilters -> FiltersList(uiState.individuals)

            is BottomSheetUiState.IndividualPointAnnotation ->
                IndividualSheet(
                    individual = uiState.individual,
                    recordTimestamp =
                        if (sheetState.targetValue == SheetValue.PartiallyExpanded) uiState.timestamp else null
                )
        }
    }
}

@Composable
fun IndividualBottomSheetRecordPointContent(timestamp: String) {
    val (date, hour) = timestamp.split('T').let { splitPart ->
        splitPart.first().replace('-', '/') to
                splitPart.last().split(':').take(2)
                    .joinToString("h")
    }
    Text(
        text = date,
        fontFamily = LocalCustomFont.current,
        fontSize = 13.sp
    )
    Text(
        text = hour,
        fontFamily = LocalCustomFont.current,
        fontSize = 13.sp
    )
}

@Composable
fun FiltersList(individuals: List<Individual>) {
    Column {
        individuals.forEach { individual ->
            Row(modifier = Modifier.fillMaxWidth()) {
                Icon(
                    painter = dynamicPainter(
                        drawableRes = R.drawable.ic_ray_icon,
                        dynamicPathMap = mapOf("dynamic_path" to individual.color),
                    ),
                    contentDescription = null,
                    tint = Color.Unspecified
                )

                Text(text = individual.individualName)
            }
        }
    }
}