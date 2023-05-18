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

package com.hdev.calendar.base

import CalendarAdapter
import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.widget.LinearLayout
import androidx.viewpager.widget.ViewPager
import androidx.viewpager.widget.ViewPager.OnPageChangeListener
import com.hdev.calendar.R
import com.hdev.calendar.bean.ClickableType
import com.hdev.calendar.bean.DateInfo
import com.hdev.calendar.bean.ViewAttrs
import com.hdev.calendar.util.Util
import com.hdev.calendar.view.WeekTitleView
import java.util.*

/**
 * 日历控件基类
 * Created by han on 2023/4/6.
 */
abstract class BaseCalendarView(
    context: Context,
    attrs: AttributeSet
) : LinearLayout(context, attrs) {

    // 当前选中的日期
    protected var selectedDate = Calendar.getInstance()
    // 最小日期
    private val minDate = Calendar.getInstance()
    // 最大日期
    private val maxDate = Calendar.getInstance()
    // 临时日期
    private val tempCalendar: Calendar = Calendar.getInstance()
    // 适配器
    private lateinit var calendarAdapter: CalendarAdapter

    private lateinit var weekTitleView: WeekTitleView

    protected lateinit var viewPager: ViewPager
    // 日历头部，包括上下月按钮，当前年月标题
    protected lateinit var headerView: BaseHeaderView

    // 控件属性
    protected lateinit var viewAttrs: ViewAttrs

    // 上下、左右间距
    private var padding: Int = Util.dp2px(context, 10f).toInt()

    // 日历点击类型
    private var clickableType = ClickableType.NORMAL

    // 可点击的日期
    var clickableDateList: List<DateInfo>? = null
        set(value) {
            checkClickableDateList()
            field = value
            value?.let {clickableType = ClickableType.CLICKABLE}
        }
    // 不可点击的日期
    var unClickableDateList: List<DateInfo>? = null
        set(value) {
            checkClickableDateList()
            field = value
            value?.let {clickableType = ClickableType.UN_CLICKABLE}
        }

    private var onPageChangeListener: OnPageChangeListener? = null

    private var pageChangeListener = object : OnPageChangeListener {
        override fun onPageScrolled(position: Int,
            positionOffset: Float,
            positionOffsetPixels: Int) {
            onPageChangeListener?.onPageScrolled(position, positionOffset, positionOffsetPixels)
        }

        override fun onPageScrollStateChanged(state: Int) {
            onPageChangeListener?.onPageScrollStateChanged(state)
        }

        override fun onPageSelected(position: Int) {
            onPageChangeListener?.onPageSelected(position)
            viewPager.findViewWithTag<BaseMonthView>(Util.getMonthViewTag(position))?.let {
                val date = DateInfo().toDate(it.monthDate)
                headerView.updateTitle(date.year, date.month)
            }
            // 是否有上一页
            if (position == 0) {
                headerView.handlePrevNext(hasPrev = false, hasNext = true)
            }
            // 是否有下一页
            if (position == calendarAdapter.monthCount - 1) {
                headerView.handlePrevNext(hasPrev = true, hasNext = false)
            }
            if (position != 0 && position != calendarAdapter.monthCount - 1) {
                headerView.handlePrevNext(hasPrev = true, hasNext = true)
            }
        }
    }

    private var headerViewListener = object : BaseHeaderView.HeaderViewListener {
        override fun prevMonth() {
            viewPager.setCurrentItem(viewPager.currentItem - 1, true)
        }

        override fun nextMonth() {
            viewPager.setCurrentItem(viewPager.currentItem + 1, true)
        }
    }

    init {
        // 设置垂直方向显示控件
        orientation = VERTICAL
        initViewAttrs(attrs)
        initViewPager()
        initHeaderView(viewAttrs.headerView)
        initWeekView()
        initDate()
        addView(viewPager)
    }

    /**
     * 初始化控件属性
     */
    protected open fun initViewAttrs(attrs: AttributeSet) {
        viewAttrs = Util.createViewAttrs(context, attrs)
    }

    /**
     * 初始化view pager
     */
    private fun initViewPager() {
        viewPager = ViewPager(context)
        viewPager.setBackgroundColor(Color.TRANSPARENT)
        calendarAdapter = CalendarAdapter(minDate) { position: Int, monthDate: Calendar ->
            val monthView = createMonthView(position, monthDate, viewAttrs)
            monthView.tag = Util.getMonthViewTag(position)
            monthView.minDate = minDate
            monthView.maxDate = maxDate
            monthView.unClickableDateList = unClickableDateList
            monthView.clickableDateList = clickableDateList
            monthView.clickableType = clickableType
            monthView.padding = padding
            monthView
        }
        viewPager.adapter = calendarAdapter
        // WARNING 动态添加view pager，需要设置id，否则方向旋转时出现以下错误
        // This usually happens when two views of different type have the same id in the same hierarchy.
        // This view's id is id/calendar_view. Make sure other views do not use the same id
        viewPager.id = R.id.view_pager
        viewPager.addOnPageChangeListener(pageChangeListener)
    }

    /**
     * 初始化头部
     */
    private fun initHeaderView(headerViewName: String?) {
        headerView = Util.createHeaderView(context, headerViewName)
        headerView.listener = headerViewListener
        addView(headerView, 0)
    }

    /**
     * 初始化周控件
     */
    private fun initWeekView() {
        weekTitleView = WeekTitleView(context, viewAttrs)
        weekTitleView.padding = padding
        addView(weekTitleView)
    }

    /**
     * 初始化默认日期
     */
    private fun initDate() {
        // 默认最小日期 1970-1-1
        minDate.set(Calendar.YEAR, 1970)
        minDate.set(Calendar.MONTH, 0)
        minDate.set(Calendar.DAY_OF_MONTH, 1)
        setDateRange(minDate.timeInMillis, maxDate.timeInMillis, selectedDate.timeInMillis)
    }

    /**
     * 设置日期范围
     * @param startTimeInMillis 开始日期，单位：毫秒
     * @param endTimeInMillis 结束日期，单位：毫秒
     * @param selectedTimeInMillis 当前日期，单位：毫秒
     */
    @JvmOverloads
    fun setDateRange(startTimeInMillis: Long = 0, endTimeInMillis: Long = 0, selectedTimeInMillis: Long = 0) {
        if (startTimeInMillis > endTimeInMillis) {
            throw IllegalArgumentException("startTimeInMillis【${startTimeInMillis}】不能大于endTimeInMillis【${endTimeInMillis}】")
        }
        if (startTimeInMillis != 0L) {
            minDate.timeInMillis = startTimeInMillis
        }
        if (endTimeInMillis != 0L) {
            maxDate.timeInMillis = endTimeInMillis
        }
        if (selectedTimeInMillis != 0L) {
            selectedDate.timeInMillis = selectedTimeInMillis
        }
        val currentDate = DateInfo().toDate(selectedDate)
        headerView.updateTitle(currentDate.year, currentDate.month)
        onRangeChanged()
    }

    /**
     * 日期范围改变
     */
    private fun onRangeChanged() {
        calendarAdapter.setRange(minDate, maxDate)

        // Changing the min/max date changes the selection position since we
        // don't really have stable IDs. Jumps immediately to the new position.
        setDate(selectedDate.timeInMillis)
    }

    private fun setDate(timeInMillis: Long) {
        var timeInMillis = timeInMillis
        var dateClamped = false
        // Clamp the target day in milliseconds to the min or max if outside the range.
        if (timeInMillis < minDate.timeInMillis) {
            timeInMillis = minDate.timeInMillis
            dateClamped = true
        } else if (timeInMillis > maxDate.timeInMillis) {
            timeInMillis = maxDate.timeInMillis
            dateClamped = true
        }
        getTempCalendarForTime(timeInMillis)
        if (dateClamped) {
            selectedDate.timeInMillis = timeInMillis
        }
        val position: Int = getPositionFromDay(timeInMillis)
        if (position != viewPager.currentItem) {
            viewPager.setCurrentItem(position, false)
        }
    }

    /**
     * 获取指定日期所在的索引号
     */
    private fun getPositionFromDay(timeInMillis: Long): Int {
        val diffMonthMax = getDiffMonths(minDate, maxDate)
        val diffMonth = getDiffMonths(minDate, getTempCalendarForTime(timeInMillis))
        return constrain(diffMonth, 0, diffMonthMax)
    }

    private fun constrain(amount: Int, low: Int, high: Int): Int {
        return if (amount < low) low else if (amount > high) high else amount
    }

    private fun getTempCalendarForTime(timeInMillis: Long): Calendar {
        tempCalendar.timeInMillis = timeInMillis
        return tempCalendar
    }

    private fun getDiffMonths(start: Calendar, end: Calendar): Int {
        val diffYears = end[Calendar.YEAR] - start[Calendar.YEAR]
        return end[Calendar.MONTH] - start[Calendar.MONTH] + 12 * diffYears
    }

    /**
     * 更新选中状态
     */
    protected fun updateDateSelected(dateInfo: DateInfo, changeMonth: Boolean, monthPosition: Int) {
        if (changeMonth) {
            viewPager.setCurrentItem(monthPosition, true)
        }
        selectedDate = dateInfo.toCalendar()
        // 更新选中状态
        updateSelected(monthPosition, dateInfo)
    }

    /**
     * 更新选中状态
     */
    protected open fun updateSelected(monthPosition: Int, date: DateInfo) {
        val childCount = viewPager.childCount
        val viewTag = Util.getMonthViewTag(monthPosition)
        for (index in 0 until childCount) {
            var monthView = viewPager.getChildAt(index) as BaseMonthView
            monthView.updateByDateSelected(monthView.tag == viewTag, date)
            monthView.invalidate()
        }
    }

    private fun checkClickableDateList() {
        if (clickableDateList != null && unClickableDateList != null) {
            throw IllegalArgumentException("不能同时设置【clickableDateList】和【unClickableDateList】属性！")
        }
    }

    /**
     * 创建月份，由子类实现
     */
    protected abstract fun createMonthView(position: Int, currentMonth: Calendar, viewAttrs: ViewAttrs): BaseMonthView

    fun setOnPageChangeListener(listener: OnPageChangeListener) {
        this.onPageChangeListener = listener
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        var width = 0
        var height = 0

        val widthMode = MeasureSpec.getMode(widthMeasureSpec)
        val widthSize = MeasureSpec.getSize(widthMeasureSpec)

        val heightMode = MeasureSpec.getMode(heightMeasureSpec)
        val heightSize = MeasureSpec.getSize(heightMeasureSpec)

        when (widthMode) {
            MeasureSpec.EXACTLY -> width = widthSize
            MeasureSpec.AT_MOST, MeasureSpec.UNSPECIFIED -> width = Util.dp2px(context, 310f).toInt()
        }
        when (heightMode) {
            MeasureSpec.EXACTLY -> height = heightSize
            MeasureSpec.AT_MOST, MeasureSpec.UNSPECIFIED -> height = Util.dp2px(context, 280f).toInt()
        }

        // 月份高度=容器-头部高度-星期标题高度
        val monthHeight = height - headerView.measuredHeight - weekTitleView.measuredHeight
        viewPager.measure(widthMeasureSpec, MeasureSpec.makeMeasureSpec(monthHeight, MeasureSpec.EXACTLY))

        setMeasuredDimension(width, height)
    }
}