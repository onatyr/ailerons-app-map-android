package fr.onat68.aileronsappmapandroid.presentation.news

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import fr.onat68.aileronsappmapandroid.data.entities.Article
import fr.onat68.aileronsappmapandroid.data.repositories.ArticleRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
class NewsViewModel @Inject constructor(private val articleRepository: ArticleRepository) :
    ViewModel() {
    val articleList: Flow<List<Article>> = articleRepository.getListArticle()
}