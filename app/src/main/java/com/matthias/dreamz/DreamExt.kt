package com.matthias.dreamz

import com.matthias.dreamz.api.*
import com.matthias.dreamz.data.model.*

fun TechMetadataDto.toModel(): TechnicalMetadata {
    return TechnicalMetadata(lastChange = this.lastChange)
}

fun DreamDayDto.toModel(uid: Long = 0): DreamDay {
    return DreamDay(
        uid = uid,
        id = this.id,
        date = this.date,
        technicalMetadata = this.techMetadata.toModel()
    )
}

fun DreamMetadataDto.toModel(): DreamMetadata {
    return DreamMetadata(
        peoples = this.peoples,
        tags = this.tags,
        lucid = this.lucid,
        note = this.note
    )
}

fun DreamDto.toModel(uid: Long = 0, dreamDayId: Long): Dream {
    return Dream(
        uid = uid,
        dreamDayId = dreamDayId,
        name = this.name,
        text = this.text,
        dreamMetadata = this.dreamMetadata.toModel(),
        id = this.id
    )
}

fun DreamMetadata.toDto(): DreamMetadataDto {
    return DreamMetadataDto(
        lucid = this.lucid,
        note = this.note,
        peoples = this.peoples,
        tags = this.tags
    )
}

fun Dream.toDto(): DreamDto {
    return DreamDto(
        name = this.name,
        text = this.text,
        id = this.id,
        dreamMetadata = this.dreamMetadata.toDto(),
    )
}

fun DreamDayWithDream.toDto(): DreamDayDto {
    return DreamDayDto(
        date = this.dreamDay.date,
        id = this.dreamDay.id,
        techMetadata = TechMetadataDto(lastChange = this.dreamDay.technicalMetadata.lastChange),
        dreams = this.dreams.map { it.toDto() }
    )
}