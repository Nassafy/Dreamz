package com.matthias.dreamz.api

import com.google.gson.annotations.SerializedName
import java.util.*

data class DreamDto(
    @SerializedName("id")
    val id: String = UUID.randomUUID().toString(),
    @SerializedName("name")
    val name: String,
    @SerializedName("text")
    val text: String,
    @SerializedName("dreamMetadata")
    val dreamMetadata: DreamMetadataDto = DreamMetadataDto()
)