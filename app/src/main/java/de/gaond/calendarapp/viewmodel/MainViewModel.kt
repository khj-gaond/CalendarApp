package de.gaond.calendarapp.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import de.gaond.calendarapp.data.Day
import de.gaond.calendarapp.data.Event
import java.text.SimpleDateFormat
import java.util.*


class MainViewModel : ViewModel() {

    private val _month = MutableLiveData<String>()
    val month: LiveData<String>
        get() = _month

    private val _days = MutableLiveData<Array<Day>>().apply { value = arrayOf() }
    val days: LiveData<Array<Day>>
        get() = _days

    private val _newEvent = MutableLiveData<Event>()
    val newEvent: LiveData<Event>
        get() = _newEvent

    fun setNewEvent(event: Event) {
        _newEvent.value = event
    }

    fun initDays() {
        val calendar = Calendar.getInstance()
        setMonthText(calendar.time)

        val thisMonth = Calendar.getInstance().apply {
            set(
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                1
            )
        }

        val prevMonth = Calendar.getInstance().apply {
            set(
                thisMonth[Calendar.YEAR],
                thisMonth[Calendar.MONTH]-1,
                1
            )
        }

        val prevPosition = thisMonth[Calendar.DAY_OF_WEEK] - 1
        val lastDayOfPrevMonth = prevMonth.getActualMaximum(Calendar.DAY_OF_MONTH)

        val dayList = mutableListOf<Day>()

        prevMonth.set(Calendar.DATE, lastDayOfPrevMonth)

        for (i in 0 until prevPosition) {
            dayList.add(0, Day(timeMillis = prevMonth.timeInMillis, date = lastDayOfPrevMonth - i))
            prevMonth.add(Calendar.DATE, -1)
        }


        val lastDayOfThisMonth = thisMonth.getActualMaximum(Calendar.DAY_OF_MONTH)

        for (i in 1..lastDayOfThisMonth) {
            dayList.add(Day(timeMillis = thisMonth.timeInMillis, date = i))
            thisMonth.add(Calendar.DATE, 1)
        }
        thisMonth.add(Calendar.DATE, -1)

        thisMonth.set(Calendar.DATE, lastDayOfThisMonth)

        val nextPosition = thisMonth[Calendar.DAY_OF_WEEK] + 1
        if (nextPosition < Calendar.DAY_OF_WEEK) {
            val nextPaddingSize = Calendar.DAY_OF_WEEK - nextPosition + 1
            for (i in 1..nextPaddingSize) {
                thisMonth.add(Calendar.DATE, 1)
                dayList.add(Day(timeMillis = thisMonth.timeInMillis, date = i))
            }
        }

        _days.value = dayList.toTypedArray()
    }

    private fun setMonthText(date: Date) {
        _month.value = monthFormat.format(date)
    }

    companion object {
        private val monthFormat = SimpleDateFormat("MM yyyy", Locale.US)
    }
}