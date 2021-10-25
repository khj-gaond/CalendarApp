package de.gaond.calendarapp.view

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.setFragmentResult
import de.gaond.calendarapp.databinding.DialogAddEventBinding
import java.text.SimpleDateFormat
import java.util.*

class AddEventDialog : DialogFragment() {
    private var _binding: DialogAddEventBinding? = null
    private val binding get() = _binding!!

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        _binding = DialogAddEventBinding.inflate(LayoutInflater.from(context))

        val args = requireArguments()
        val timeMillis = args.getLong(KEY_TIME, 0)
        val requestCode = args.getString(KEY_REQUEST, "")

        val builder = AlertDialog.Builder(requireContext()).apply {
            val calendar = Calendar.getInstance()
            calendar.timeInMillis = timeMillis
            val date = dateFormat.format(calendar.time)

            setTitle(date)
            setView(binding.root)

            binding.confirm.setOnClickListener {
                if (!binding.edit.text.isNullOrEmpty()) {
                    val bundle = Bundle().apply {
                        putString(KEY_RESULT_EVENT_TEXT, binding.edit.text.toString())
                        putLong(KEY_RESULT_EVENT_TIME, timeMillis)
                    }
                    setFragmentResult(requestCode, bundle)
                }
                dismiss()
            }
        }

        return builder.create()
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        const val KEY_TIME = "key_time"
        const val KEY_REQUEST = "key_request"
        const val KEY_RESULT_EVENT_TEXT = "key_result_text"
        const val KEY_RESULT_EVENT_TIME = "key_result_date"

        private val dateFormat = SimpleDateFormat("E, d L", Locale.US)

        internal fun newInstance(timeMillis: Long, requestCode: String): AddEventDialog {
            val bundle = Bundle().apply {
                putLong(KEY_TIME, timeMillis)
                putString(KEY_REQUEST, requestCode)
            }

            val dialog = AddEventDialog()
            dialog.arguments = bundle
            return dialog
        }
    }
}