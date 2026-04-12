package fr.ailerons.map.presentation.screens.map

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import fr.ailerons.map.data.entities.Individual
import fr.ailerons.map.data.repositories.IndividualRepository
import fr.ailerons.map.data.repositories.RecordPointRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.merge
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed interface BottomSheetParams {
    data class IndividualPointAnnotation(
        val idIndividual: Int,
        val timestamp: String,
    ) : BottomSheetParams

    data object IndividualFilters : BottomSheetParams
}

sealed interface BottomSheetUiState {
    data class IndividualPointAnnotation(
        val individual: Individual,
        val timestamp: String,
    ) : BottomSheetUiState

    data class IndividualFilters(
        val individuals: List<Individual>,
        val displayedIndividualIds: StateFlow<List<Int>>,
        val toggleVisibility: (idIndividual: Int, isVisible: Boolean) -> Unit
    ) : BottomSheetUiState
}

@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class MapViewModel @Inject constructor(
    private val recordPointRepository: RecordPointRepository,
    private val individualRepository: IndividualRepository,
) : ViewModel() {

    private val _bottomSheetUiState = MutableStateFlow<BottomSheetUiState?>(null)
    val bottomSheetUiState = _bottomSheetUiState.asStateFlow()

    private val _idIndividualsToShow = MutableSharedFlow<List<Int>>()
    private val displayedIndividualIds = merge(
        individualRepository.getAllIds(),
        _idIndividualsToShow
    )
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.Eagerly,
            initialValue = emptyList()
        )


    val recordPoints = displayedIndividualIds.flatMapLatest {
        recordPointRepository.getWithColorByIds(it)
    }

    val colors = individualRepository.getColors()

    suspend fun setIdIndividualFilter(id: Int) = _idIndividualsToShow.emit(listOf(id))

    fun updateBottomSheetUiState(params: BottomSheetParams?) =
        viewModelScope.launch(Dispatchers.IO) {
            when (params) {
                is BottomSheetParams.IndividualFilters -> {
                    _bottomSheetUiState.update {
                        BottomSheetUiState.IndividualFilters(
                            individuals = individualRepository.getAllWithNonEmptyRecordPoints()
                                .first(),
                            displayedIndividualIds = displayedIndividualIds,
                            toggleVisibility =
                                { idIndividual, isVisible ->
                                    viewModelScope.launch {
                                        displayedIndividualIds.value.let { currentList ->
                                            _idIndividualsToShow.emit(
                                                if (isVisible) currentList.plus(idIndividual)
                                                    .distinct()
                                                else currentList.minus(idIndividual)
                                            )
                                        }
                                    }
                                }

                        )
                    }
                }

                is BottomSheetParams.IndividualPointAnnotation -> {
                    _bottomSheetUiState.update {
                        individualRepository.getById(params.idIndividual).first()
                            ?.let { individual ->
                                BottomSheetUiState.IndividualPointAnnotation(
                                    individual = individual,
                                    timestamp = params.timestamp
                                )
                            }
                    }
                }

                null -> _bottomSheetUiState.update { null }
            }
        }
}