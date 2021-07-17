package com.matthias.dreamz.data.adapter

import androidx.room.TypeConverter
import java.time.Instant
import java.time.LocalDate
import java.time.OffsetDateTime
import java.util.*

class LocalDateConverter {
    @TypeConverter
    fun fromTimestamp(value: Long?): LocalDate? {
        return if (value == null) null else Instant.ofEpochMilli(value)
            .atOffset(OffsetDateTime.now().offset).toLocalDate()
    }

    @TypeConverter
    fun dateToTimestamp(date: LocalDate?): Long? {
        return date?.atStartOfDay()?.toInstant(OffsetDateTime.now().offset)?.toEpochMilli()
    }
}