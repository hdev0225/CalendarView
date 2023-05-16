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
import com.hdev.calendar.listener.IDateRange
import com.hdev.calendar.listener.OnDateRangeSelectedListener
import com.hdev.calendar.listener.OnDateSelectedListener
import com.hdev.calendar.view.month.RangeMonthView
import java.util.*

/**
 * 范围选择日历控件
 * Created by han on 2023/4/20.
 */
class RangeCalendarView(
    context: Context,
    attrs: AttributeSet
) : BaseCalendarView(context, attrs) {

    // 开始日期
    private var startDate: DateInfo? = null
    // 结束日期
    private var endDate: DateInfo? = null

    private var listener: OnDateRangeSelectedListener? = null

    /**
     * 创建月份
     */
    override fun createMonthView(position: Int, currentMonth: Calendar, viewAttrs: ViewAttrs): BaseMonthView {
        var monthView = RangeMonthView(context, currentMonth, position, viewAttrs)
        monthView.setDateRange(startDate, endDate)
        monthView.onDateSelectedListener = object : OnDateSelectedListener {
            override fun onDateSelected(dateInfo: DateInfo, changeMonth: Boolean, monthPosition: Int) {
                setupDate(monthView, dateInfo)
                updateDateSelected(dateInfo, changeMonth, monthPosition)

                // 回调到调用者
                if (startDate != null && endDate != null) {
                    listener?.let {it(this@RangeCalendarView, dateInfo, startDate!!, endDate!!)}
                }

                // 显示日期范围
                showDateRange(startDate, endDate)
            }
        }
        return monthView
    }

    /**
     * 显示日期范围
     */
    private fun showDateRange(startDate: DateInfo?, endDate: DateInfo?) {
        // header view是否实现接口 IDateRange
        if (headerView is IDateRange) {
            (headerView as IDateRange).dateRange(startDate, endDate)
        }
    }

    /**
     * 设置日期
     */
    private fun setupDate(monthView: RangeMonthView, dateInfo: DateInfo) {
        // 开始日期和结束日期不为空，表示重新选择日期
        if (startDate != null && endDate != null) {
            startDate = null
            endDate = null
        }
        if (startDate == null) {
            // 设置开始日期
            startDate = dateInfo
        } else if (endDate == null) {
            // 设置结束日期
            endDate = dateInfo
        }
        if (startDate != null && endDate != null) {
            // 开始日期大于结束日期，交换位置
            if (startDate!! > endDate!!) {
                val tmpDate = startDate
                startDate = endDate
                endDate = tmpDate
            } else if (startDate == endDate) {
                // 开始结束日期，不能一样
                startDate = null
                endDate = null
            }
        }
        monthView.setDateRange(startDate, endDate)
    }

    /**
     * 更新选中状态
     */
    override fun updateSelected(monthPosition: Int, date: DateInfo) {
        val childCount = viewPager.childCount
        for (index in 0 until childCount) {
            var monthView = viewPager.getChildAt(index) as RangeMonthView
            monthView.setDateRange(startDate, endDate)
            monthView.invalidate()
        }
    }

    /**
     * 设置选中的日期范围
     */
    fun setSelectedDateRange(startDate: DateInfo, endDate: DateInfo) {
        this.startDate = startDate
        this.endDate = endDate
        showDateRange(this.startDate, this.endDate)
        setDateRange(selectedTimeInMillis = startDate.timeInMillis())
    }

    fun setOnDateRangeSelectedListener(listener: OnDateRangeSelectedListener) {
        this.listener = listener
    }
}