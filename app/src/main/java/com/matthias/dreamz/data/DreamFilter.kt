package com.matthias.dreamz.data

data class DreamFilter(
    val tags: List<String>,
    val peoples: List<String>,
    val text: String,
    val lucid: Boolean
)