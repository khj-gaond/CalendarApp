package de.gaond.calendarapp.view.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import de.gaond.calendarapp.data.Day
import de.gaond.calendarapp.data.Event
import de.gaond.calendarapp.data.setEvent
import de.gaond.calendarapp.databinding.ItemDayBinding
import de.gaond.calendarapp.databinding.ItemEventBinding


class CalendarAdapter(private val clickListener: (day: Day?) -> Unit) : RecyclerView.Adapter<CalendarAdapter.ViewHolder>() {
    private var days: Array<Day> = arrayOf()

    private var rowCount: Int = 0

    fun setDays(days: Array<Day>) {
        this.days = days

        rowCount = itemCount / 7

        notifyDataSetChanged()
    }

    fun dataChanged(time: Long, eventTitle: String) {
        for (i in days.indices) {
            if (days[i].timeMillis == time) {
                days[i].setEvent(Event(time, eventTitle))
                notifyItemChanged(i)
                break
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemDayBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        with(binding.root) {
            post {
                layoutParams.height = parent.height / rowCount
                requestLayout()
            }
        }

        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.initView(days[position])
        holder.bind(days[position], clickListener)
    }

    override fun getItemCount(): Int {
        return days.size
    }

    class ViewHolder(private val binding: ItemDayBinding) : RecyclerView.ViewHolder(binding.root) {
        fun initView(item: Day) {
            if (item.event != null && binding.eventContainer.childCount == 0) {
                val eventBinding = ItemEventBinding.inflate(LayoutInflater.from(binding.eventContainer.context), binding.eventContainer, true)
                eventBinding.eventText = item.event!!.title
            }
        }

        fun bind(item: Day, listener: (day: Day?) -> Unit) {
            with(binding) {
                clickListener = View.OnClickListener { listener(binding.day) }
                day = item
                executePendingBindings()
            }
        }
    }

}