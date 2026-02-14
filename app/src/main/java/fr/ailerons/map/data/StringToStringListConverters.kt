package fr.ailerons.map.data

import androidx.room.TypeConverter


class StringToStringListConverters {
    @TypeConverter
    fun fromStringList(list: List<String>?): String? = list?.joinToString(",")

    @TypeConverter
    fun toStringList(data: String?): List<String>? = data?.split(",")
}