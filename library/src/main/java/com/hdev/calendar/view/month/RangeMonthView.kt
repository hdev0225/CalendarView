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
import com.hdev.calendar.bean.ClickableType
import com.hdev.calendar.bean.DateInfo
import com.hdev.calendar.bean.DateItem
import com.hdev.calendar.bean.RangeViewAttrs
import java.util.*

/**
 * 日期区间选择
 * Created by han on 2023/4/27.
 */
internal class RangeMonthView(
    context: Context,
    monthDate: Calendar, // 月份日期
    positionInCalendar: Int = 0, // 当前月份所在的日历索引
    private var rangeViewAttrs: RangeViewAttrs,
) : BaseMonthView(context, monthDate, positionInCalendar, rangeViewAttrs) {

    // 开始日期
    private var startDate: DateInfo? = null
    // 结束日期
    private var endDate: DateInfo? = null

    private fun setNormalPaintColor(date: DateInfo) {
        selectedPaint.color = rangeViewAttrs.selectedRangeDayColor
    }

    override fun setClickablePaintColor(date: DateInfo) {
        // 可点击日期中是否存在该日期，如果存在，则画笔设置高亮
        if (clickableDateList?.contains(date) == true) {
            selectedPaint.color = rangeViewAttrs.selectedRangeDayColor
        } else {
            selectedPaint.color = viewAttrs.selectedDayDimColor
        }
    }

    override  fun setUnClickablePaintColor(date: DateInfo) {
        // 不可点击日期中是否存在该日期，如果存在，则画笔设置灰色
        if (unClickableDateList?.contains(date) == true) {
            selectedPaint.color = viewAttrs.selectedDayDimColor
        } else {
            selectedPaint.color = rangeViewAttrs.selectedRangeDayColor
        }
    }

    /**
     * 绘制选中日期
     */
    override fun drawSelectedDay(canvas: Canvas?, dateItem: DateItem): Boolean {
        if (startDate != null && endDate != null) {
            var handleSelectedDate = false
            // 当前月份日期
            if (dateItem.date.type == DateInfo.DateType.CURRENT) {
                // 设置中间选中区域字体颜色
                if (dateItem.date > startDate!! && dateItem.date < endDate!!) {
                    when (clickableType) {
                        ClickableType.NORMAL -> setNormalPaintColor(dateItem.date)
                        ClickableType.CLICKABLE -> setClickablePaintColor(dateItem.date)
                        ClickableType.UN_CLICKABLE -> setUnClickablePaintColor(dateItem.date)
                    }
                    handleSelectedDate = true
                } else if (dateItem.date == startDate!!) { // 设置开始日期字体颜色
                    selectedPaint.color = rangeViewAttrs.selectedStartDayColor
                    handleSelectedDate = true
                } else if ( dateItem.date == endDate!!) { // 设置结束日期字体颜色
                    selectedPaint.color = rangeViewAttrs.selectedEndDayColor
                    handleSelectedDate = true
                } else { // 默认颜色
                    selectedPaint.color = rangeViewAttrs.defaultColor
                }
            } else { // 其他月份日期
                // 默认颜色
                selectedPaint.color = rangeViewAttrs.defaultDimColor
            }
            drawDayText(canvas, dateItem, selectedPaint)
            return handleSelectedDate
        } else if (startDate != null) {
            // 本月份的日子才绘制高亮
            if (startDate?.type == dateItem.date.type && startDate!! == dateItem.date) {
                selectedPaint.color = rangeViewAttrs.selectedStartDayColor
                drawDayText(canvas, dateItem, selectedPaint)
                return true
            }
        }

        return false
    }

    /**
     * 绘制选中背景
     */
    override fun drawSelectedBg(canvas: Canvas?) {
        super.drawSelectedBg(canvas)
        // 绘制区间
        if (startDate != null && endDate != null) {
            selectedPaint.color = viewAttrs.selectedBgColor
            dateItemList.forEach {
                if (it.date.type == DateInfo.DateType.CURRENT) {
                    // 绘制左半圆
                    drawLeftArcBg(canvas, it)
                    // 绘制区间背景
                    drawRangeBg(canvas, it)
                    // 绘制右半圆
                    drawRightArcBg(canvas, it)
                }
            }
        } else if (startDate != null) {
            dateItemList.forEach {
                if (startDate?.type == it.date.type && startDate!! == it.date) {
                    selectedPaint.color = rangeViewAttrs.selectedStartBgColor
                    canvas?.drawCircle(it.centerPoint.x, it.centerPoint.y, selectedRadius, selectedPaint)
                }
            }
        }
    }

    /**
     * 绘制区间背景
     */
    private fun drawRangeBg(canvas: Canvas?, dateItem: DateItem) {
        // 在startDate和endDate之间的日期绘制矩形
        selectedPaint.color = rangeViewAttrs.selectedRangeBgColor
        if (dateItem.date > startDate!! && dateItem.date < endDate!!) {
            canvas?.drawRect(dateItem.cellBounds.left, dateItem.clickBounds.top,
                dateItem.cellBounds.right, dateItem.clickBounds.bottom, selectedPaint)
        }
    }

    /**
     * 绘制左半圆
     */
    private fun drawLeftArcBg(canvas: Canvas?, dateItem: DateItem) {
        //  绘制开始日期
        if (dateItem.date == startDate) {
            // 选中的日期与下一个日期之间的空白位置
            selectedPaint.color = rangeViewAttrs.selectedRangeBgColor
            canvas?.drawRect(dateItem.clickBounds.right, dateItem.clickBounds.top,
                dateItem.clickBounds.right + (dateItem.cellBounds.right - dateItem.clickBounds.right),
                dateItem.clickBounds.bottom, selectedPaint)

            // 左半圆
            selectedPaint.color = rangeViewAttrs.selectedStartBgColor
            canvas?.drawArc(dateItem.clickBounds.left, dateItem.clickBounds.top,
                dateItem.clickBounds.right, dateItem.clickBounds.bottom, -90f,
                -180f, true, selectedPaint)
            // 左半圆后面的位置
            canvas?.drawRect(dateItem.centerPoint.x - 1, dateItem.clickBounds.top,
                dateItem.clickBounds.right, dateItem.clickBounds.bottom, selectedPaint)
        }
    }

    /**
     * 绘制右半圆
     */
    private fun drawRightArcBg(canvas: Canvas?, dateItem: DateItem) {
        // 绘制结束日期
        if (dateItem.date == endDate) {
            // 选中的日期与前一个日期之间的空白位置
            selectedPaint.color = rangeViewAttrs.selectedRangeBgColor
            canvas?.drawRect(dateItem.cellBounds.left, dateItem.clickBounds.top,
                dateItem.cellBounds.left + (dateItem.cellBounds.right - dateItem.clickBounds.right),
                dateItem.clickBounds.bottom, selectedPaint)

            // 右半圆前面的位置
            selectedPaint.color = rangeViewAttrs.selectedEndBgColor
            canvas?.drawRect(dateItem.clickBounds.left, dateItem.clickBounds.top,
                dateItem.centerPoint.x + 1, dateItem.clickBounds.bottom, selectedPaint)
            // 右半圆
            canvas?.drawArc(dateItem.clickBounds.left, dateItem.clickBounds.top,
                dateItem.clickBounds.right, dateItem.clickBounds.bottom, -90f,
                180f, true, selectedPaint)
        }

    }

    /**
     * 选中日期，RangeCalendarView.onDateSelected()
     */
    override fun onDateSelected(selectedDateItem: DateItem, changeMonth: Boolean, monthPosition: Int) {
        super.onDateSelected(selectedDateItem, changeMonth, monthPosition)
        var selectedDate = selectedDateItem.date
        var newSelectedDate = DateInfo(selectedDate.year, selectedDate.month, selectedDate.day)
        // 如果选择的日期不是当月的日期
        if (selectedDateItem.date.type != DateInfo.DateType.CURRENT) {
            newSelectedDate.type = DateInfo.DateType.CURRENT
        }
        // 日期选择回调，回调到RangeCalendarView
        onDateSelectedListener?.onDateSelected(newSelectedDate, changeMonth, monthPosition)
    }

    /**
     * 设置日期范围
     */
    fun setDateRange(startDate: DateInfo?, endDate: DateInfo?) {
        this.startDate = startDate
        this.endDate = endDate
    }
}