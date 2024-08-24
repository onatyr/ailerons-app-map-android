package fr.onat68.aileronsappmapandroid.presentation.news

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import fr.onat68.aileronsappmapandroid.data.entities.Article
import fr.onat68.aileronsappmapandroid.presentation.NavRoute

@Composable
fun NewsScreen(newsViewModel: NewsViewModel, navigate: (NavRoute) -> Unit) {
    Column(modifier = Modifier.background(Color.White)) {
        val articles = newsViewModel.articleList.collectAsState(emptyList())

        Column {
            articles.value.forEach {
                ArticleCard(
                    article = it,
                    modifier = Modifier
                        .padding(8.dp)
                        .clickable { navigate(it) }
                )
            }
        }

    }
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun ArticleCard(article: Article, modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(16.dp))
            .fillMaxWidth()
    ) {
        GlideImage(model = article.imageUrl, contentDescription = "article image")
        Column(modifier = Modifier
            .align(Alignment.BottomStart)
            .padding(horizontal = 8.dp)) {
            Title(title = article.title, textColor = Color.White)
            PublicationDate(date = formatDate(article.publicationDate), textColor = Color.White)
        }
    }
}