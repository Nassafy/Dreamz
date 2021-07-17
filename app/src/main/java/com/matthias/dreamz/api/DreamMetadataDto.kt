package com.matthias.dreamz.api

import com.google.gson.annotations.SerializedName

data class DreamMetadataDto(
    @SerializedName("lucid")
    val lucid: Boolean = false,
    @SerializedName("note")
    var note: Int = 0,
    @SerializedName("tags")
    val tags: List<String> = listOf(),
    @SerializedName("peoples")
    val peoples: List<String> = listOf()
)
