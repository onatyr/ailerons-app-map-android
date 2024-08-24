package fr.onat68.aileronsappmapandroid.presentation.news

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import fr.onat68.aileronsappmapandroid.R
import fr.onat68.aileronsappmapandroid.data.entities.Article
import fr.onat68.aileronsappmapandroid.presentation.LocalCustomFont
import fr.onat68.aileronsappmapandroid.presentation.ScrollableColumnWithHeader
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale

fun formatDate(input: String): String {
    val dateTime = LocalDateTime.parse(input)
    val formatter = DateTimeFormatter.ofPattern("dd MMMM yyyy", Locale.FRENCH)
    return dateTime.format(formatter)
        .split(" ")
        .toMutableList()
        .let {
            it[1] = it[1].replaceFirstChar { firstChar ->
                firstChar.uppercaseChar()
            }
            return@let it
        }.joinToString(" ")
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun ArticleScreen(article: Article) {
    ScrollableColumnWithHeader(
        headerLabel = stringResource(R.string.article),
    ) {
        GlideImage(model = article.imageUrl, contentDescription = null)
        Column(modifier = Modifier.padding(horizontal = 16.dp)) {
            Title(title = article.title, Color(0xFF173B65))
            PublicationDate(date = formatDate(article.publicationDate))
            Text(text = article.content)
        }
    }
}

@Composable
fun Title(title: String, textColor: Color) {
    Text(
        text = title,
        fontSize = 30.sp,
        color = textColor,
        fontFamily = LocalCustomFont.current,
        modifier = Modifier.padding(top = 12.dp)
    )
}

@Composable
fun PublicationDate(date: String, textColor: Color = Color.Unspecified) {
    Text(text = "Equipe Ailerons $date", modifier = Modifier.padding(bottom = 12.dp), color = textColor)
}