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

package com.hdev.calendar.view.month

import android.content.Context
import android.graphics.Canvas
import com.hdev.calendar.base.BaseMonthView
import com.hdev.calendar.bean.DateInfo
import com.hdev.calendar.bean.DateItem
import com.hdev.calendar.bean.ViewAttrs
import java.util.*

/**
 * 默认单选月份View
 * Created by han on 2023/4/10.
 */
internal open class SingleMonthView(
    context: Context,
    monthDate: Calendar, // 月份日期
    positionInCalendar: Int = 0, // 当前月份所在的日历索引
    viewAttrs: ViewAttrs,
) : BaseMonthView(context, monthDate, positionInCalendar, viewAttrs) {

    // 选中的日期信息
    protected var selectedDateItem: DateItem? = null
    // 选中的日子
    var selectedDate: DateInfo? = null

    override fun afterDataInit() {
        super.afterDataInit()
        // 设置默认选中的日期
        dateItemList.forEach {
            if (it.date == selectedDate) {
                selectedDateItem = it
            }
        }
    }

    /**
     * 绘制选中日期
     */
    override fun drawSelectedDay(canvas: Canvas?, dateItem: DateItem): Boolean {
        super.drawSelectedDay(canvas, dateItem)
        return drawSelectedDay(canvas, dateItem, selectedDateItem)
    }

    /**
     * 绘制选中日期
     */
    protected open fun drawSelectedDay(canvas: Canvas?, dateItem: DateItem, selectedDateItem: DateItem?): Boolean {
        // 本月份的日子才绘制高亮
        return selectedDateItem?.let {
            if (it.date.type == DateInfo.DateType.CURRENT && it === dateItem) {
                selectedPaint.color = viewAttrs.selectedDayColor
                drawDayText(canvas, dateItem, selectedPaint)
                true
            } else {
                false
            }
        } ?: false
    }

    /**
     * 绘制选中背景
     */
    override fun drawSelectedBg(canvas: Canvas?) {
        super.drawSelectedBg(canvas)
        drawSelectedBg(canvas, selectedDateItem)
    }

    /**
     * 绘制选中背景
     */
    protected open fun drawSelectedBg(canvas: Canvas?, selectedDateItem: DateItem?) {
        super.drawSelectedBg(canvas)
        selectedDateItem?.let {
            // 本月份的日子才绘制高亮
            if (it.date.type == DateInfo.DateType.CURRENT) {
                selectedPaint.color = viewAttrs.selectedBgColor
                canvas?.drawCircle(it.centerPoint.x, it.centerPoint.y, selectedRadius, selectedPaint)
            }
        }
    }
    
    /**
     * 选中日期，SingleCalendarView.onDateSelected()，再调用本类的updateByDateSelected
     */
    override fun onDateSelected(selectedDateItem: DateItem, changeMonth: Boolean, monthPosition: Int) {
        super.onDateSelected(selectedDateItem, changeMonth, monthPosition)
        // 选中的日子是否为本月
        if (selectedDateItem.date.type != DateInfo.DateType.CURRENT) {
            // 取消本月选中
            this.selectedDateItem = null
        }
        // 日期选择回调
        onDateSelectedListener?.onDateSelected(selectedDateItem.date, changeMonth, monthPosition)
    }

    /**
     * 根据选中的日期，更新界面
     */
    override fun updateByDateSelected(isCurrentMonth: Boolean, dateInfo: DateInfo) {
        super.updateByDateSelected(isCurrentMonth, dateInfo)
        if (isCurrentMonth) {
            // 取消选中状态
            selectedDate = null
            selectedDateItem = null
            for (item in dateItemList) {
                if (item.date == dateInfo) {
                    selectedDate = dateInfo
                    selectedDateItem = item
                    break
                }
            }
        } else {
            selectedDate = null
            selectedDateItem = null
        }
    }
}