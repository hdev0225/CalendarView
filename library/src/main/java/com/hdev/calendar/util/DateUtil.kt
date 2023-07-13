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

package com.hdev.calendar.util

import android.content.Context
import android.graphics.PointF
import android.graphics.RectF
import com.hdev.calendar.bean.DateInfo
import com.hdev.calendar.bean.DateItem
import com.hdev.calendar.constant.Const
import java.util.*

/**
 * Date util
 * Created by han on 2023/4/6.
 */
object DateUtil {

    /**
     * 检查当前日期是否为今天
     * @param systemDate 系统日期
     * @param monthDate 当前月份的年月信息
     * @param day 当前月份的日子
     */
    fun isToday(systemDate: Calendar, monthDate: Calendar, day: Int): Boolean {
        if (systemDate == null || monthDate == null) {
            return false
        }
        return systemDate.get(Calendar.YEAR) == monthDate.get(Calendar.YEAR)
                && systemDate.get(Calendar.MONTH) == monthDate.get(Calendar.MONTH)
                && systemDate.get(Calendar.DAY_OF_MONTH) == day

    }

    /**
     * 是否为过去的日期
     * @param minDate 最小值
     * @param monthDate 当前月份的年月信息
     * @param day 当前月份的日子
     */
    fun isPastDays(minDate: Calendar, monthDate: Calendar, day: Int): Boolean {
        // 年月相等，日子小于最小日子
        if (minDate.get(Calendar.YEAR) == monthDate.get(Calendar.YEAR)
            && minDate.get(Calendar.MONTH) == monthDate.get(Calendar.MONTH)
            && day < minDate.get(Calendar.DAY_OF_MONTH)) {
            return true
        }
        // 年份相等，月份小於最大月份
        if (monthDate.get(Calendar.YEAR) == minDate.get(Calendar.YEAR)
            && monthDate.get(Calendar.MONTH) < minDate.get(Calendar.MONTH)) {
            return true
        }
        // 当前年份小于最小年份
        if (monthDate.get(Calendar.YEAR) < minDate.get(Calendar.YEAR)) {
            return true
        }
        return false
    }

    /**
     * 是否为将来日期
     * @param maxDate 最大值
     * @param monthDate 当前月份的年月信息
     * @param day 当前月份的日子
     */
    fun isFutureDays(maxDate: Calendar, monthDate: Calendar, day: Int): Boolean {
        // 當前年份大於最大年份
        if (monthDate.get(Calendar.YEAR) > maxDate.get(Calendar.YEAR)) {
            return true
        }
        // 年份相等，月份大於最大月份
        if (monthDate.get(Calendar.YEAR) == maxDate.get(Calendar.YEAR)
            && monthDate.get(Calendar.MONTH) > maxDate.get(Calendar.MONTH)) {
            return true
        }
        // 年月与最大值相同，日子大于最大值日子，则为未来日子
        if (maxDate.get(Calendar.YEAR) == monthDate.get(Calendar.YEAR)
            && maxDate.get(Calendar.MONTH) == monthDate.get(Calendar.MONTH)
            && day > maxDate.get(Calendar.DAY_OF_MONTH)) {
            return true
        }

        return false
    }

    /**
     * 当前日期是否已超出范围
     */
    fun isOutOfRange(minDate: Calendar?, maxDate: Calendar?, monthDay: Calendar, day: Int): Boolean {
        if (minDate == null || maxDate == null) {
            return false
        }

        return isPastDays(minDate, monthDay, day) || isFutureDays(maxDate, monthDay, day)
    }

    /**
     * 是否为选中的日期
     * @param monthDate 当前月份的年月信息
     * @param selectedDate 选中的日期
     * @param day 当前月份的日子
     */
    fun isSelectedDate(monthDate: Calendar, selectedDate: Calendar, day: Int): Boolean {
        if (monthDate == null || selectedDate == null) {
            return false
        }
        return monthDate.get(Calendar.YEAR) == selectedDate.get(Calendar.YEAR)
                && monthDate.get(Calendar.MONTH) == selectedDate.get(Calendar.MONTH)
                && day == selectedDate.get(Calendar.DAY_OF_MONTH)
    }

    /**
     * 获取当月第一天或者最后一天是星期几，1: 星期一， 7：星期天
     * @param isFirstDay true: 第一天； false: 最后一天
     */
    fun getDayOfWeekInMonth(timeInMillis: Long, isFirstDay: Boolean): Int {
        var calendar = Calendar.getInstance()
        calendar.timeInMillis = timeInMillis

        calendar[Calendar.DATE] = 1
        if (!isFirstDay) {
            // 日期回滚一天，也就是最后一天
            calendar.roll(Calendar.DATE, -1)
        }
        var dayOfWeek = calendar[Calendar.DAY_OF_WEEK]
        // 星期天用7表示
        if (dayOfWeek == 1) {
            dayOfWeek = 7
        } else {
            dayOfWeek -= 1
        }
        return dayOfWeek
    }

    /**
     * 获取当月天数
     */
    fun getDaysOfMonth(timeInMillis: Long): Int {
        var calendar = Calendar.getInstance()
        calendar.timeInMillis = timeInMillis
        calendar[Calendar.DATE] = 1
        calendar.roll(Calendar.DATE, -1)
        return calendar[Calendar.DAY_OF_MONTH]
    }

    /**
     * 判断是否为周末
     */
    fun isWeekend(date: DateInfo): Boolean {
         var calendar = Calendar.getInstance()
        calendar[Calendar.YEAR] = date.year
        calendar[Calendar.MONTH] = date.month - 1
        calendar[Calendar.DAY_OF_MONTH] = date.day
        var dayOfWeek = calendar[Calendar.DAY_OF_WEEK]
        return dayOfWeek == Calendar.SATURDAY || dayOfWeek == Calendar.SUNDAY
    }

    /**
     * 构造日期item,包容每项绘制坐标，中心坐标，日期点击范围
     */
    fun buildDateItemList(context: Context, dateList: List<DateInfo>, containerWidth: Int, cellHeight: Int, textHeight: Int, padding: Int = 0): MutableList<DateItem> {
        val maxRadius = Util.dp2px(context, 26f)
        var cellWidth = (containerWidth - padding * 2) / Const.COLUMNS_PER_WEEK
        var dateItemList = mutableListOf<DateItem>()
        // 文字绘制坐标
        var textDrawLeft: Float
        var textDrawTop: Float = (cellHeight - textHeight) / 2.0f
        // 文字中心坐标
        var textCenterLeft: Float
        var textCenterTop: Float = cellHeight / 2.0f
        // 格子坐标
        var cellLeft: Float

        var halfBox = cellHeight / 2.5f
        if (halfBox > maxRadius) {
            halfBox = maxRadius
        }

        for (index in dateList.indices) {
            val date = dateList[index]

            textDrawLeft = padding + cellWidth / 2.0f + (index % Const.COLUMNS_PER_WEEK) * cellWidth
            textCenterLeft = textDrawLeft
            cellLeft = (padding + (index % Const.COLUMNS_PER_WEEK) * cellWidth).toFloat()
            // 换行绘制
            if (index != 0 && index % Const.COLUMNS_PER_WEEK == 0) {
                textDrawTop += cellHeight
                textCenterTop += cellHeight
            }
            // 点击区域
            val clickBounds = RectF(textCenterLeft - halfBox, textCenterTop - halfBox,
                textCenterLeft + halfBox, textCenterTop + halfBox)
            // 文本绘制坐标
            val drawPoint = PointF(textDrawLeft, textDrawTop)
            // 中心坐标
            val centerPoint = PointF(textCenterLeft, textCenterTop)
            // 格子区域
            var cellBounds = RectF(cellLeft, textCenterTop - halfBox,
                cellLeft + cellWidth, textCenterTop + halfBox)

            var dateItem = DateItem(date, drawPoint, centerPoint, clickBounds, cellBounds)

            dateItemList.add(dateItem)
        }
        return dateItemList
    }

    /**
     * 构建星期数据
     * 星期信息, key为第几星期，该星期相对当前日历，也即当前月份的第几行；value为该星期(该行)包含日期
     */
    fun buildWeekMap(dateItemList: List<DateItem>): MutableMap<Int, MutableList<DateItem>> {
        var weekMap: MutableMap<Int, MutableList<DateItem>> = mutableMapOf()
        var tmpDateItemList: MutableList<DateItem> = mutableListOf()
        var week = 1
        for (index in dateItemList.indices) {
            var dateItem = dateItemList[index]
            // 设置每第一星期(即每一行)包括的dateItem
            if (index % Const.COLUMNS_PER_WEEK == 0) {
                tmpDateItemList = mutableListOf()
                weekMap[week] = tmpDateItemList
                week++
            }
            tmpDateItemList.add(dateItem)
        }
        return weekMap
    }

    /**
     * 计数星期数
     */
    fun calcWeekCount(dateList: List<DateInfo>): Int {
        var week = 0
        for (index in dateList.indices) {
            // 设置每第一星期(即每一行)包括的dateItem
            if (index % Const.COLUMNS_PER_WEEK == 0) {
                week++
            }
        }
        return week
    }

    /**
     * 构建月份日期
     * @param timeInMillis 日历时间戳
     * @param theFirstDayOfWeek 用户设置一周的第一天是星期几，默认为1
     */
    fun buildDateList(timeInMillis: Long, theFirstDayOfWeek: Int = 1): List<DateInfo> {
        var calendar = Calendar.getInstance()
        calendar.timeInMillis = timeInMillis

        var dateList = mutableListOf<DateInfo>()
        // 本月天数
        var daysOfMonth = getDaysOfMonth(timeInMillis)
        // 本月第一天是星期几
        var firstDayOfWeek = getDayOfWeekInMonth(timeInMillis, true)
        // 本月最后一天是星期几
        var lastDayOfWeek = getDayOfWeekInMonth(timeInMillis, false)

        // 添加本月日期
        for (day in 1 .. daysOfMonth) {
            var year = calendar[Calendar.YEAR]
            var month = calendar[Calendar.MONTH] + 1
            var date = DateInfo(year, month, day)
            date.type = DateInfo.DateType.CURRENT
            dateList.add(date)
        }

        // 根据theFirstDayOfWeek构建星期数据
        val weekList = buildWeekByFirstDayOfWeek(theFirstDayOfWeek)

        // 日历前面需要补的天数
        val firstFixDays = getDayIndexInWeek(firstDayOfWeek, weekList)
        // 日历后面需要补的天数
         val lastFixDays = getDayIndexInWeek(lastDayOfWeek, weekList)

        // 日历前面有空位，则用上一个月的日期补足本星期
        buildPrevDate(timeInMillis, firstFixDays, dateList)

        // 日历后面有空位，则用下一个月的日期补足本星期
        buildNextDate(timeInMillis, lastFixDays, dateList)

        // 星期行数为6行，如果少于6行，则补足6行
        fixWeek(dateList)
        return dateList
    }

    /**
     * 构建月份日期
     * @param timeInMillis 日历时间戳
     * @param fixDays 需要补足的天数
     * @param dateList 当前月份日期列表
     */
    private fun buildPrevDate(timeInMillis: Long, fixDays: Int, dateList: MutableList<DateInfo>) {
        // 日历前面有空位，则用上个月的日期补足本星期
        if (fixDays != 0) {
            val calendar = Calendar.getInstance()
            calendar.timeInMillis = timeInMillis
            // 设置上一月份
            calendar[Calendar.MONTH] = calendar[Calendar.MONTH] - 1
            val daysOfMonth = getDaysOfMonth(calendar.timeInMillis)
            val year = calendar[Calendar.YEAR]
            val month = calendar[Calendar.MONTH] + 1
            for (index in 1 .. fixDays) {
                var date = DateInfo(year, month, daysOfMonth - index + 1)
                date.type = DateInfo.DateType.PREV
                dateList.add(0, date)
            }
        }
    }

    /**
     * 构建月份日期
     * @param timeInMillis 日历时间戳
     * @param fixDays 需要补足的天数
     * @param dateList 当前月份日期列表
     */
    private fun buildNextDate(timeInMillis: Long, fixDays: Int, dateList: MutableList<DateInfo>) {
        if (fixDays != 7) {
            val calendar = Calendar.getInstance()
            calendar.timeInMillis = timeInMillis
            // 设置下一月份
            calendar[Calendar.MONTH] = calendar[Calendar.MONTH] + 1
            val year = calendar[Calendar.YEAR]
            val month = calendar[Calendar.MONTH] + 1
            for (index in 1 until 7 - fixDays) {
                val date = DateInfo(year, month, index)
                date.type = DateInfo.DateType.NEXT
                dateList.add(date)
            }
        }
    }

    /**
     * 构建星期数据
     * @param firstDayOfWeek 一周的第一天，1表示星期一，7表示星期天
     * 当theFirstDateOfWeek为7时，返回[7, 1, 2, 3, 4, 5, 6]
     * 当theFirstDateOfWeek为3时，返回[3, 4, 5, 6, 7, 1, 2]
     * 当theFirstDateOfWeek为6时，返回[6, 7, 1, 2, 3, 4, 5]
     */
    private fun buildWeekByFirstDayOfWeek(firstDayOfWeek: Int): List<Int> {
        val daysOfWeek = 7
        val weekList = mutableListOf<Int>()
        for (day in firstDayOfWeek .. daysOfWeek) {
            weekList.add(day)
        }
        // 不足7天，补够7天
        if (weekList.size < daysOfWeek) {
            for (day in 1 .. daysOfWeek - weekList.size) {
                weekList.add(day)
            }
        }
        return weekList
    }

    /**
     * 获取日期在一周中的索引号
     * @param dayOfWeek 星期几
     * 如第一天为星期日，weekList=[7, 1, 2, 3, 4, 5, 6]，dayOfWeek=5，索引号为5，当前日历前面需要补5天
     * 如第一天为星期三，weekList=[3, 4, 5, 6, 7, 1, 2]，dayOfWeek=5，索引号为2，当前日历前面需要补2天
     */
    private fun getDayIndexInWeek(dayOfWeek: Int, weekList: List<Int>): Int {
        for (index in 0 .. weekList.size) {
            if (dayOfWeek == weekList[index]) {
                return index
            }
        }
        return 0
    }

    private fun fixWeek(dateList: MutableList<DateInfo>) {
        // 计算现有星期数
        var weekLines = 0
        for (index in dateList.indices) {
            if (index.rem(Const.COLUMNS_PER_WEEK) == 0) {
                ++weekLines
            }
        }
        // 取出列表中最后的日期，不足6行，补足6行
        var lastDate = dateList[dateList.size - 1]
        var calendar = lastDate.toCalendar()
        // 记录已添加的个数
        var count = 0
        // 行数少于6行，一直循环，直到等于6行
        while (weekLines < Const.WEEK_LINES) {
            ++count
            calendar[Calendar.DAY_OF_MONTH] = calendar[Calendar.DAY_OF_MONTH] + 1
            var date = DateInfo().toDate(calendar)
            date.type = DateInfo.DateType.NEXT
            dateList.add(date)
            if (count.rem(Const.COLUMNS_PER_WEEK) == 0) {
                ++weekLines
            }
        }
    }

    /**
     * 获取某一周
     * @param n 0表示本周; -1表示上1周, -2表示上两周；  1表示下一周，2表示下两周
     * @param date 日期
     */
    fun getWeek(n: Int = 0, date: DateInfo): List<DateInfo> {
        val dateList = mutableListOf<DateInfo>()
        val calendar = Calendar.getInstance()
        calendar.firstDayOfWeek = Calendar.MONDAY
        calendar[Calendar.YEAR] = date.year
        calendar[Calendar.MONTH] = date.month - 1
        calendar[Calendar.DAY_OF_MONTH] = date.day

        // 判断要计算的日期是否是周日，如果是则减一天计算周六的，否则会出问题，计算到下一周了
        val dayWeek = calendar[Calendar.DAY_OF_WEEK]
        if (Calendar.SUNDAY == dayWeek) {
            calendar.add(Calendar.DAY_OF_MONTH, -1)
        }

        // 设置一个星期的第一天，一个星期的第一天是星期一
        calendar.firstDayOfWeek = Calendar.MONDAY
        // 获得当前日期是一个星期的第几天
        val day = calendar[Calendar.DAY_OF_WEEK]
        // 根据日历的规则，给当前日期减去星期几与一个星期第一天的差值
        calendar.add(Calendar.DATE, calendar.firstDayOfWeek - day + 7 * n)
        dateList.add(DateInfo().toDate(calendar))
        for (i in 1..6) {
            calendar.add(Calendar.DATE, 1)
            dateList.add(DateInfo().toDate(calendar))
        }
        return dateList
    }

    /**
     * 当前日期的上一周
     * @param date 当前日期
     */
    fun getPrevWeek(date: DateInfo) = getWeek(-1, date)

    /**
     * 当前日期的下一周
     * @param date 当前日期
     */
    fun getNextWeek(date: DateInfo) = getWeek(1, date)

    /**
     * 根据年月，获取当月的日期
     */
    fun getDateList(dateMap: MutableMap<Pair<Int, Int>, MutableList<DateInfo>>?, year: Int, month: Int): MutableList<DateInfo>? {
        return dateMap?.let {
            it[Pair(year, month)]
        }
    }

    /**
     * 根据年月，构建月份信息
     * key为年月，value为
     */
    fun buildDateMap(dateList: MutableList<DateInfo>?): MutableMap<Pair<Int, Int>, MutableList<DateInfo>>? {
        return dateList?.let {
            var resultMap = HashMap<Pair<Int, Int>, MutableList<DateInfo>>()
            it.forEach { date ->
                var yearMonth = Pair(date.year, date.month)
                var resultList = resultMap[yearMonth]
                if (resultList == null) {
                    resultList = arrayListOf()
                    resultMap[yearMonth] = resultList
                }
                resultList.add(date)

            }
            resultMap
        }
    }
}