package fr.ailerons.map.presentation.lib

import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import fr.ailerons.map.R

val LocalCustomFont = staticCompositionLocalOf {
    FontFamily(
        Font(R.font.atkinson_hyperlegible_regular, FontWeight.Companion.Normal),
        Font(R.font.atkinson_hyperlegible_italic, style = FontStyle.Companion.Italic),
        Font(R.font.atkinson_hyperlegible_bold, FontWeight.Companion.Bold)
    )
}

val LocalPopBackStack = compositionLocalOf { {} }