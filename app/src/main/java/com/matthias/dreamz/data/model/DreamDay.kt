package com.matthias.dreamz.data.model

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.Relation
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.*

@Entity
data class DreamDay(
    @PrimaryKey(autoGenerate = true) val uid: Long = 0,
    val id: String = UUID.randomUUID().toString(),
    val date: LocalDate,
    @Embedded(prefix = "tech")
    val technicalMetadata: TechnicalMetadata = TechnicalMetadata()
)

@Entity
data class TechnicalMetadata(
    @PrimaryKey(autoGenerate = true) val uid: Long = 0,
    val lastChange: LocalDateTime? = null
)