package fr.onat68.aileronsappmapandroid.data

import androidx.room.TypeConverter


class StringToStringListConverters {
    @TypeConverter
    fun fromStringList(list: List<String>?): String? = list?.joinToString(",")

    @TypeConverter
    fun toStringList(data: String?): List<String>? = data?.split(",")
}