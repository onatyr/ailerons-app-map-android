package fr.ailerons.map.presentation

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import fr.ailerons.map.data.repositories.ArticleRepository
import fr.ailerons.map.data.repositories.IndividualRepository
import fr.ailerons.map.data.repositories.RecordPointRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class MainActivityViewModel @Inject constructor(
    private val individualRepository: IndividualRepository,
    private val recordPointRepository: RecordPointRepository,
    private val articleRepository: ArticleRepository,
) : ViewModel() {

    suspend fun fetchData() = withContext(Dispatchers.IO) {
        listOf(
            async {
                individualRepository.fetchFromRemote()
                recordPointRepository.fetchFromRemote()
            },
            async {
                articleRepository.fetchFromRemote()
            }
        ).awaitAll()
    }
}