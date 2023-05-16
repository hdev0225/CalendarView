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
import android.graphics.Color
import com.hdev.calendar.bean.ClickableType
import com.hdev.calendar.bean.DateInfo
import com.hdev.calendar.bean.DateItem
import com.hdev.calendar.bean.ViewAttrs
import com.hdev.calendar.util.Util
import java.util.*

/**
 * 月份View，可选中某一星期
 * Created by han on 2023/4/12.
 */
internal class WeekMonthView(
    context: Context,
    monthDate: Calendar, // 月份日期
    positionInCalendar: Int = 0, // 当前月份所在的日历索引
    viewAttrs: ViewAttrs,
) : SingleMonthView(context, monthDate, positionInCalendar, viewAttrs) {

    // 选中的日子(selectedDate)所在的星期的第一天
    var startDateItem: DateItem? = null
        private set
    // 选中的日子(selectedDate)所在的星期的最后一天
    var endDateItem: DateItem? = null
        private set
    // 圆角
    private var roundCorners: Float = Util.dp2px(context, 30f)
    // 某一周的dateItem
    private var weekDateItemList: List<DateItem>? = null

    override fun afterDataInit() {
        super.afterDataInit()
        // 数据初始化完毕后，计算开始和结束日期
        selectedDateItem?.let { calcStartAndEndDate(it) }
    }

    /**
     * 绘制选中日期
     */
    override fun drawSelectedDay(canvas: Canvas?, dateItem: DateItem): Boolean {
        // 本月份的日子才绘制高亮
        if (selectedDateItem?.date?.type == DateInfo.DateType.CURRENT) {
            weekDateItemList?.let { dataItemList ->
                if (dataItemList.contains(dateItem)) {
                    when (clickableType) {
                        ClickableType.NORMAL -> selectedPaint.color = viewAttrs.selectedDayColor
                        ClickableType.CLICKABLE -> setClickablePaintColor(dateItem.date)
                        ClickableType.UN_CLICKABLE -> setUnClickablePaintColor(dateItem.date)
                    }
                    drawDayText(canvas, dateItem, selectedPaint)
                    return true
                }
            }
        }
        return false
    }

    /**
     * 绘制选中背景
     */
    override fun drawSelectedBg(canvas: Canvas?) {
        if (selectedDateItem?.date?.type == DateInfo.DateType.CURRENT) {
            // 本月份的日子才绘制高亮
            if (startDateItem != null && endDateItem != null) {
                selectedPaint.color = viewAttrs.selectedBgColor
                canvas?.drawRoundRect(startDateItem!!.clickBounds.left, startDateItem!!.clickBounds.top,
                    endDateItem!!.clickBounds.right, endDateItem!!.clickBounds.bottom, roundCorners, roundCorners, selectedPaint)
            }
        }
    }

    /**
     * 选中日期，WeekCalendarView.onDateSelected()，再调用本类的updateByDateSelected
     */
    override fun onDateSelected(selectedDateItem: DateItem, changeMonth: Boolean, monthPosition: Int) {
        calcStartAndEndDate(selectedDateItem)
        super.onDateSelected(selectedDateItem, changeMonth, monthPosition)
    }

    /**
     * 选中的日期是所在的星期，获取该星期的的起始和结束日期
     */
    private fun calcStartAndEndDate(selectedDateItem: DateItem) {
        weekMap.values.filter {
            it.contains(selectedDateItem)
        }.let {
            this.weekDateItemList = it[0]
            this.weekDateItemList?.let { dateItemList ->
                if (dateItemList.isNotEmpty()) {
                    this.startDateItem = dateItemList[0]
                    this.endDateItem = dateItemList[dateItemList.size - 1]
                }
            }
        }
    }

    /**
     * 根据选中的日期，更新界面
     */
    override fun updateByDateSelected(isCurrentMonth: Boolean, dateInfo: DateInfo) {
        super.updateByDateSelected(isCurrentMonth, dateInfo)
        if (isCurrentMonth) {
            selectedDateItem?.let {
                calcStartAndEndDate(it)
            }
        } else {
            selectedDate = null
            selectedDateItem = null
            startDateItem = null
            endDateItem = null
            weekDateItemList = null
        }
    }
}