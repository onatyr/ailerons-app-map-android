package fr.onat68.aileronsappmapandroid.presentation.news

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.max
import androidx.compose.ui.unit.sp
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import fr.onat68.aileronsappmapandroid.data.entities.Article
import fr.onat68.aileronsappmapandroid.presentation.NavRoute

@Composable
fun NewsScreen(newsViewModel: NewsViewModel, navigate: (NavRoute) -> Unit) {
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .background(Color.White)
            .verticalScroll(scrollState)
    ) {
        val articles = newsViewModel.articleList.collectAsState(emptyList())

        Column {
            articles.value.forEachIndexed { index, article ->
                ArticleCard(
                    article = article,
                    modifier = Modifier
                        .padding(8.dp)
                        .clickable {
                            navigate(article)
                        },
                    extended = (index == 0)
                )
            }
        }
    }
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun ArticleCard(
    article: Article,
    modifier: Modifier = Modifier,
    extended: Boolean = false
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(16.dp))
            .fillMaxWidth()
            .height(if (extended) 250.dp else 90.dp)
    ) {
        GlideImage(
            model = article.imageUrl,
            contentDescription = "article image",
            modifier = Modifier
                .heightIn(max = 250.dp),
            contentScale = ContentScale.Crop
        )
        Column(
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .align(Alignment.BottomStart)
                .height(90.dp)
                .background(Color.Black.copy(alpha = 0.2f))
                .padding(horizontal = 8.dp)
                .fillMaxWidth()
        ) {
            Title(
                title = article.title,
                textColor = Color.White,
                maxLines = 2,
                fontSize = 20.sp
            )
            PublicationDate(
                date = formatDate(article.publicationDate),
                textColor = Color.White
            )
        }
    }
}