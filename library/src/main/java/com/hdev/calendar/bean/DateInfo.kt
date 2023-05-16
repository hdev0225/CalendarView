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

package com.hdev.calendar.bean

import java.text.SimpleDateFormat
import java.util.*

/**
 * Date Info
 * Created by han on 2023/4/6.
 */
data class DateInfo(
    var year: Int = 1900,
    var month: Int = 1, /* 1-12月，1月从1开始*/
    var day: Int = 1,
) {

    // 日历中的月份包括上一个月，本月月份，下一个月的日子
    // 当前日子属于本月，上一月份还是下一月份
    var type: DateType = DateType.CURRENT

    fun toCalendar(): Calendar {
        var calendar = Calendar.getInstance()
        calendar[Calendar.YEAR] = year
        calendar[Calendar.MONTH] = month - 1
        calendar[Calendar.DAY_OF_MONTH] = day
        return calendar
    }

    fun timeInMillis(): Long {
        return toCalendar().timeInMillis
    }

    fun toDate(calendar: Calendar): DateInfo {
        this.year = calendar[Calendar.YEAR]
        this.month = calendar[Calendar.MONTH] + 1
        this.day = calendar[Calendar.DAY_OF_MONTH]
        return this
    }

    /**
     * 格式化日期yyyy-MM-dd
     */
    fun format(): String {
        var monthStr = month.toString()
        var dayStr = day.toString()
        if (month < 10) {
            monthStr = "0$month"
        }
        if (day < 10) {
            dayStr = "0$day"
        }
        return StringBuilder().apply {
            append(year).append("-").append(monthStr).append("-").append(dayStr)
        }.toString()
    }

    operator fun compareTo(date: DateInfo): Int {
        val sdf = SimpleDateFormat("yyyy-MM-dd")
        val thisDate = sdf.parse("$year-$month-$day")
        val otherDate = sdf.parse("${date.year}-${date.month}-${date.day}")
        if (thisDate.after(otherDate)) {
            return 1
        } else if (thisDate.before(otherDate)) {
            return -1
        }
        return 0
    }

    enum class DateType {
        PREV, CURRENT, NEXT
    }
}