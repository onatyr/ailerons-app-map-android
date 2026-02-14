package fr.ailerons.map.presentation.individual

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import fr.ailerons.map.data.entities.Individual
import fr.ailerons.map.data.repositories.IndividualRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
class IndividualViewModel @Inject constructor(private val individualRepository: IndividualRepository) :
    ViewModel() {

    val individualsList: Flow<List<Individual>> = individualRepository.getListIndividual()
}