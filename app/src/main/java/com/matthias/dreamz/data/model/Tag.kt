package com.matthias.dreamz.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Tag(
    @PrimaryKey(autoGenerate = true) val uid: Long,
    val tagType: TagType,
    val tag: String,
    val quantity: Int
)

enum class TagType {
    TAG,
    PEOPLE
}