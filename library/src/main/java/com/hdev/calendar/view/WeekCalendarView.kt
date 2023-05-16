/*
 * MIT License
 *
 * Copyright (c) 2023 hdev0225
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 *
 */

package com.hdev.calendar.view

import android.content.Context
import android.util.AttributeSet
import com.hdev.calendar.base.BaseCalendarView
import com.hdev.calendar.base.BaseMonthView
import com.hdev.calendar.bean.DateInfo
import com.hdev.calendar.bean.ViewAttrs
import com.hdev.calendar.listener.OnDateRangeSelectedListener
import com.hdev.calendar.listener.OnDateSelectedListener
import com.hdev.calendar.view.month.WeekMonthView
import java.util.*

/**
 * 按星期选择日期控件
 * Created by han on 2023/4/9.
 */
class WeekCalendarView(
    context: Context,
    attrs: AttributeSet
) : BaseCalendarView(context, attrs) {

    private var listener: OnDateRangeSelectedListener? = null

    /**
     * 设置选中日期
     */
    fun setSelectedDate(selectedDate: DateInfo) {
        setDateRange(selectedTimeInMillis = selectedDate.timeInMillis())
    }

    /**
     * 创建月份
     */
    override fun createMonthView(position: Int, currentMonth: Calendar, viewAttrs: ViewAttrs): BaseMonthView {
        val monthView = WeekMonthView(context, currentMonth, position, viewAttrs)
        monthView.selectedDate = DateInfo().toDate(selectedDate)
        monthView.onDateSelectedListener = object : OnDateSelectedListener {
            override fun onDateSelected(dateInfo: DateInfo, changeMonth: Boolean, monthPosition: Int) {
                listener?.let { it(this@WeekCalendarView, dateInfo,
                    monthView.startDateItem?.date!!, monthView.endDateItem?.date!!) }
                updateDateSelected(dateInfo, changeMonth, monthPosition)
            }
        }
        return monthView
    }

    fun setOnDateRangeSelectedListener(listener: OnDateRangeSelectedListener) {
        this.listener = listener
    }
}