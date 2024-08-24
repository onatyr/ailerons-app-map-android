package fr.onat68.aileronsappmapandroid.presentation.article

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import fr.onat68.aileronsappmapandroid.R
import fr.onat68.aileronsappmapandroid.data.entities.Article
import fr.onat68.aileronsappmapandroid.presentation.Header

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun ArticleScreen(article: Article) {
    Column {
        Header(label = stringResource(R.string.article))

        GlideImage(model = article.imageUrl, contentDescription = null)
    }
}