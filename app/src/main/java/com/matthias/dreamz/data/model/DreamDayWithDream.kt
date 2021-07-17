package com.matthias.dreamz.data.model

import androidx.room.Embedded
import androidx.room.Relation

data class DreamDayWithDream(
    @Embedded
    val dreamDay: DreamDay,
    @Relation(entityColumn = "dreamDayId", parentColumn = "uid")
    val dreams: List<Dream>
)
