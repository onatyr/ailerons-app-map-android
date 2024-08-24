package fr.onat68.aileronsappmapandroid.presentation.news

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.platform.LocalContext
import fr.onat68.aileronsappmapandroid.R

@Composable
fun NewsScreen(newsViewModel: NewsViewModel) {
    Text(LocalContext.current.resources.getString(R.string.news))
    val articles = newsViewModel.articleList.collectAsState(emptyList())
    
    Text(text = articles.value.first().title)
}