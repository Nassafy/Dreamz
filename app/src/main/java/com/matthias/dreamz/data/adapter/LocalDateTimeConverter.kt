package com.matthias.dreamz.data.adapter

import androidx.room.TypeConverter
import java.time.Instant
import java.time.LocalDateTime
import java.time.OffsetDateTime

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