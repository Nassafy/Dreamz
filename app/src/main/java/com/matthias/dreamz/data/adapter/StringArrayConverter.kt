package com.matthias.dreamz.data.adapter

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken


class StringArrayConverter {
    @TypeConverter()
    fun fromList(value: List<String>): String {
        return Gson().toJson(value)
    }

    @TypeConverter
    fun toList(value: String): List<String> {
        val typeToken = object : TypeToken<List<String>>() {}.type
        return Gson().fromJson(value, typeToken)
    }
}