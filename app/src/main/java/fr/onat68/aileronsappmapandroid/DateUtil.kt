package fr.onat68.aileronsappmapandroid

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale

object DateUtil {

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

}