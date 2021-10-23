package com.matthias.dreamz.ui.screen.dreamlist

import com.matthias.dreamz.data.model.Dream
import com.matthias.dreamz.data.model.DreamDayWithDream
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import java.util.*

data class DreamDayState(
    val date: String,
    val dreams: List<DreamState>,
    val id: Long,
    val lucid: Boolean
)

data class DreamState(val name: String, val note: Int, val lucid: Boolean)

internal fun Dream.toState(): DreamState {
    return DreamState(
        name = this.name,
        note = this.dreamMetadata.note,
        lucid = this.dreamMetadata.lucid
    )
}

internal fun DreamDayWithDream.toState(): DreamDayState {
    val dateFormatter =
        DateTimeFormatter.ofLocalizedDate(FormatStyle.LONG)
            .withLocale(Locale.getDefault())
            .withZone(ZoneId.systemDefault())

    val date =
        this.dreamDay.date

    val lucid = this.dreams.find { it.dreamMetadata.lucid } != null
    return DreamDayState(
        id = this.dreamDay.uid,
        date = date.format(dateFormatter),
        dreams = this.dreams.map { it.toState() },
        lucid = lucid
    )
}