package fr.onat68.aileronsappmapandroid.presentation.map

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import fr.onat68.aileronsappmapandroid.data.repositories.RecordPointRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class MapViewModel @Inject constructor(
    private val recordPointRepository: RecordPointRepository,
) : ViewModel() {

    private val _individualIdFilter = MutableStateFlow<Int?>(null)
    private val individualIdFilter = _individualIdFilter.asStateFlow()

    val recordPoints = individualIdFilter.flatMapLatest {
        if (it == null) recordPointRepository.getAll()
        else recordPointRepository.getByIdIndividual(it)
    }

    fun setIndividualIdFilter(id: Int?) = _individualIdFilter.update { id }
}