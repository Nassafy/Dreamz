package com.matthias.dreamz.data.adapter

import androidx.room.TypeConverter
import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.OffsetDateTime
import java.util.*

class LocalDateTimeConverter {
    @TypeConverter
    fun fromTimestamp(value: Long?): LocalDateTime? {
        return if (value == null) null else Instant.ofEpochMilli(value)
            .atOffset(OffsetDateTime.now().offset).toLocalDateTime()
    }

    @TypeConverter
    fun dateToTimestamp(date: LocalDateTime?): Long? {
        return date?.toInstant(OffsetDateTime.now().offset)?.toEpochMilli()
    }
}