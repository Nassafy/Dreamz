package com.matthias.dreamz.data.model

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(indices = [Index(value = ["tagType", "count"])])
data class TagInfo(
    @PrimaryKey(autoGenerate = true) val uid: Long = 0,
    val name: String,
    val count: Int,
    val tagType: TagType
)
