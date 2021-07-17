package com.matthias.dreamz.ui.screen.viewdream

import com.matthias.dreamz.data.model.Dream
import com.matthias.dreamz.data.model.DreamDay
import com.matthias.dreamz.data.model.DreamDayWithDream
import com.matthias.dreamz.data.model.DreamMetadata
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import java.util.*

data class DreamDayState(val date: String, val dreams: List<DreamState>, val id: Long)

data class BaseDreamDayState(val date: String, val id: Long)

data class DreamState(val name: String, val text: String, val id: Long, val metadata: DreamMetadata)

internal fun Dream.toState(): DreamState {
    return DreamState(
        name = this.name,
        text = this.text,
        id = this.uid,
        metadata = this.dreamMetadata
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
        dreams = this.dreams.map { it.toState() })
}

internal fun DreamDay.toState(): BaseDreamDayState {
    val dateFormatter =
        DateTimeFormatter.ofLocalizedDate(FormatStyle.LONG)
            .withLocale(Locale.getDefault())
            .withZone(ZoneId.systemDefault())

    val date =
        this.date
    return BaseDreamDayState(
        id = this.uid,
        date = date.format(dateFormatter))
}