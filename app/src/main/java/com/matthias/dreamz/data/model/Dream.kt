package com.matthias.dreamz.data.model

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity
data class Dream(
    @PrimaryKey(autoGenerate = true) val uid: Long = 0,
    val id: String = UUID.randomUUID().toString(),
    val name: String = "",
    val text: String = "",
    val dreamDayId: Long,
    @Embedded
    val dreamMetadata: DreamMetadata = DreamMetadata(),
)