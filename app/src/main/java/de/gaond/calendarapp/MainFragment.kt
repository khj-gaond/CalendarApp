package de.gaond.calendarapp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import de.gaond.calendarapp.view.AddEventDialog.Companion.KEY_REQUEST
import de.gaond.calendarapp.data.Day
import de.gaond.calendarapp.data.Event
import de.gaond.calendarapp.databinding.FragmentMainBinding
import de.gaond.calendarapp.view.AddEventDialog
import de.gaond.calendarapp.view.AddEventDialog.Companion.KEY_RESULT_EVENT_TEXT
import de.gaond.calendarapp.view.AddEventDialog.Companion.KEY_RESULT_EVENT_TIME
import de.gaond.calendarapp.view.adapter.CalendarAdapter
import de.gaond.calendarapp.viewmodel.MainViewModel

class MainFragment : Fragment() {
    private val viewModel: MainViewModel by viewModels()

    private var _binding: FragmentMainBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMainBinding.inflate(LayoutInflater.from(context), container, false)
        binding.lifecycleOwner = viewLifecycleOwner

        binding.calendar.layoutManager = GridLayoutManager(requireContext(), DAY_OF_WEEK)

        parentFragmentManager.setFragmentResultListener(KEY_REQUEST, viewLifecycleOwner) { key, bundle ->
            if (key == KEY_REQUEST) {
                val event = Event(
                        bundle.getLong(KEY_RESULT_EVENT_TIME, 0L),
                        bundle.getString(KEY_RESULT_EVENT_TEXT, "")
                    )
                viewModel.setNewEvent(event)
            }
        }

        val clickListener = fun(day: Day?) {
            day?.let {
                val dialog = AddEventDialog.newInstance(it.timeMillis, KEY_REQUEST)
                dialog.show(parentFragmentManager, KEY_REQUEST)
            }
        }

        val adapter = CalendarAdapter(clickListener)
        binding.calendar.adapter = adapter

        binding.vm = viewModel

        viewModel.initDays()

        subscribeUI(adapter)

        return binding.root
    }

    private fun subscribeUI(adapter: CalendarAdapter) {
        viewModel.days.observe(viewLifecycleOwner) {
            adapter.setDays(it)
        }
        
        viewModel.newEvent.observe(viewLifecycleOwner) {
            adapter.dataChanged(it.time, it.title)
        }
    }

    companion object {
        fun newInstance() = MainFragment()
        private const val DAY_OF_WEEK = 7
    }
}