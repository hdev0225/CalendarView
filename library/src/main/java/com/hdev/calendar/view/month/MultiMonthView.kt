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
import com.hdev.calendar.bean.DateInfo
import com.hdev.calendar.bean.DateItem
import com.hdev.calendar.bean.ViewAttrs
import java.util.*

/**
 * 多选月份View
 * Created by han on 2023/4/27.
 */
internal class MultiMonthView(
    context: Context,
    monthDate: Calendar,
    positionInCalendar: Int = 0, // 当前月份所在的日历索引
    viewAttrs: ViewAttrs,
    var selectedDateList: MutableList<DateInfo>
) : SingleMonthView(context, monthDate, positionInCalendar, viewAttrs) {

    // 保存选中的日期列表项
    private val selectedDateItemList: MutableList<DateItem> = mutableListOf()

    override fun afterDataInit() {
        dateItemList.forEach {
            if (it.date == selectedDate) {
                selectedDateItem = it
            }
            if (selectedDateList.contains(it.date)) {
                selectedDateItemList.add(it)
            }
        }
    }

    /**
     * 绘制选中日期
     */
    override fun drawSelectedDay(canvas: Canvas?, dateItem: DateItem): Boolean {
        if (selectedDateItemList.contains(dateItem)) {
            return drawSelectedDay(canvas, dateItem, dateItem)
        }
        return false
    }

    /**
     * 绘制选中背景
     */
    override fun drawSelectedBg(canvas: Canvas?) {
        for (item in selectedDateItemList) {
            drawSelectedBg(canvas, item)
        }
    }

    /**
     * 根据选中的日期，更新界面
     */
    override fun updateByDateSelected(isCurrentMonth: Boolean, dateInfo: DateInfo) {
        super.updateByDateSelected(isCurrentMonth, dateInfo)
        if (isCurrentMonth) {
            var dateItem = dateItemList.filter {it.date == dateInfo }
            // 取消选中
            if (selectedDateList.contains(dateInfo)) {
                selectedDateItemList.remove(dateItem[0])
                selectedDateList.remove(dateInfo)
            } else {
                // 设置选中
                selectedDateItemList.add(dateItem[0])
                selectedDateList.add(dateInfo)
            }
        }
    }
}