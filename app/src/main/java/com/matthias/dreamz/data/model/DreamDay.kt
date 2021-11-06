package com.matthias.dreamz.data.model

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.Instant
import java.time.LocalDate
import java.util.*

@Entity
data class DreamDay(
    @PrimaryKey(autoGenerate = true) val uid: Long = 0,
    val id: String = UUID.randomUUID().toString(),
    val date: LocalDate,
    @Embedded(prefix = "tech")
    val technicalMetadata: TechnicalMetadata = TechnicalMetadata(lastChange = Instant.now())
)

data class TechnicalMetadata(
    val lastChange: Instant
)