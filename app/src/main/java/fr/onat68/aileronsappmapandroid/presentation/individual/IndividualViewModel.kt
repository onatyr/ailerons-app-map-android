package fr.onat68.aileronsappmapandroid.presentation.individual

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import fr.onat68.aileronsappmapandroid.data.entities.Individual
import fr.onat68.aileronsappmapandroid.data.repositories.IndividualRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
class IndividualViewModel @Inject constructor(private val individualRepository: IndividualRepository) :
    ViewModel() {

    val individualsList: Flow<List<Individual>> = individualRepository.getListIndividual()
}