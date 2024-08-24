package fr.onat68.aileronsappmapandroid.presentation.news

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import fr.onat68.aileronsappmapandroid.presentation.NavRoute

@Composable
fun NewsScreen(newsViewModel: NewsViewModel, navigate: (NavRoute) -> Unit) {
    Column {
        val articles = newsViewModel.articleList.collectAsState(emptyList())

        Column {
            articles.value.forEach {
                Button(onClick = { navigate(it) }) {
                    Text(text = it.title)
                }
            }
        }

    }
}