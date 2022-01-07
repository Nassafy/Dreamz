package com.matthias.dreamz.ui.screen.editdream

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
    val uuid: String
)

data class DreamState(
    val name: String,
    val text: String,
    val textNote: String?,
    val id: Long,
    val lucid: Boolean = false
)

internal fun Dream.toState(): DreamState {
    return DreamState(
        name = this.name,
        text = this.text,
        textNote = this.textNote,
        id = this.uid,
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
    return DreamDayState(
        id = this.dreamDay.uid,
        date = date.format(dateFormatter),
        dreams = this.dreams.map { it.toState() },
        uuid = this.dreamDay.id
    )
}