package com.matthias.dreamz.data.model

data class DreamMetadata(
    val lucid: Boolean = false,
    var note: Int = 0,
    val tags: List<String> = listOf(),
    val peoples: List<String> = listOf()
)
