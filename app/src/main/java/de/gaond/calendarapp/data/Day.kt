package de.gaond.calendarapp.data

data class Day(
    val timeMillis: Long,
    val date: Int,
    var event: Event? = null
)

fun Day.setEvent(event: Event) {
    this.event = event
}