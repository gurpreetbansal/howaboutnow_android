package com.how_about_now.app.utils

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context

import java.util.Calendar
import java.util.Date

/**
 * Created by Anuj.kamboj on 14/05/2019.
 * Utility class to show default date and time dialog and handle callback
 */

class DateTimePickerDialog {
    private var showTimePicker = true
    private var showDatePicker = true
    private var defaultDate = Date()
    private var dateTimeSetListener: DateTimeSetListener? = null

    fun build(context: Context, dateSetListener: DateTimeSetListener) {
        dateTimeSetListener = dateSetListener
        val calendar = Calendar.getInstance()
        calendar.time = defaultDate
        if (showDatePicker) {
            val datePickerDialog =
                DatePickerDialog(context, DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
                    val calendar = Calendar.getInstance()
                    calendar.time = defaultDate
                    calendar.set(Calendar.YEAR, year)
                    calendar.set(Calendar.MONTH, monthOfYear)
                    calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                    defaultDate = calendar.time
                    if (showTimePicker)
                        showTimePickerDialog(context)
                    else if (dateTimeSetListener != null)
                        dateTimeSetListener!!.onDateTimeSet(defaultDate)
                }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH))
            datePickerDialog.show()
        } else if (showTimePicker) {
            showTimePickerDialog(context)
        }
    }

    private fun showTimePickerDialog(context: Context) {
        val calendar = Calendar.getInstance()
        calendar.time = defaultDate
        val timePickerDialog = TimePickerDialog(context, TimePickerDialog.OnTimeSetListener { view, hourOfDay, minute ->
            val calendar = Calendar.getInstance()
            calendar.time = defaultDate
            calendar.set(Calendar.HOUR_OF_DAY, hourOfDay)
            calendar.set(Calendar.MINUTE, minute)
            defaultDate = calendar.time
            if (dateTimeSetListener != null)
                dateTimeSetListener!!.onDateTimeSet(defaultDate)
        }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), false)
        timePickerDialog.show()
    }

    fun showTimePicker(showTimePicker: Boolean): DateTimePickerDialog {
        this.showTimePicker = showTimePicker
        return this
    }

    fun showDatePicker(showDatePicker: Boolean): DateTimePickerDialog {
        this.showDatePicker = showDatePicker
        return this
    }

    fun setDefaultDate(defaultDate: Date): DateTimePickerDialog {
        this.defaultDate = defaultDate
        return this
    }

}

