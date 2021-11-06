package com.matthias.dreamz.api

import com.google.gson.annotations.SerializedName
import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.*


data class DreamDayDto(
    @SerializedName("id")
    val id: String,
    @SerializedName("date")
    val date: LocalDate,
    @SerializedName("dreams")
    val dreams: List<DreamDto> = listOf(),
    @SerializedName("techMetadata")
    val techMetadata: TechMetadataDto
)

data class TechMetadataDto(
    @SerializedName("lastChange")
    val lastChange: Instant
)