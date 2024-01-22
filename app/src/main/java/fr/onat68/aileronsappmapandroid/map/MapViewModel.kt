package fr.onat68.aileronsappmapandroid.map

import fr.onat68.aileronsappmapandroid.RecordPoints
import kotlinx.coroutines.flow.Flow

class MapViewModel(val recordPoints: Flow<List<RecordPoints>>)