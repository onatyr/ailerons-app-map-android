package fr.ailerons.map.presentation.screens.news

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import fr.ailerons.map.data.entities.Article
import fr.ailerons.map.data.repositories.ArticleRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
class NewsViewModel @Inject constructor(private val articleRepository: ArticleRepository) :
    ViewModel() {
    val articleList: Flow<List<Article>> = articleRepository.getListArticle()
}