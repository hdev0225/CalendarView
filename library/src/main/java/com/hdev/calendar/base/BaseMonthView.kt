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

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.view.MotionEvent
import android.view.View
import com.hdev.calendar.bean.ClickableType
import com.hdev.calendar.bean.DateInfo
import com.hdev.calendar.bean.DateItem
import com.hdev.calendar.bean.ViewAttrs
import com.hdev.calendar.listener.OnDateSelectedListener
import com.hdev.calendar.util.DateUtil
import java.util.*

/**
 * 月份基类
 * Created by han on 2023/3/5.
 */
abstract class BaseMonthView (
    context: Context,
    val monthDate: Calendar, // 月份日期
    var positionInCalendar: Int = 0, // 当前月份所在的日历索引
    var viewAttrs: ViewAttrs
) : View(context) {

    private var dataInit = false
    // 每一格子高度
    private var cellHeight: Int = 0
    // 文字大小
    private val fontSize: Float = viewAttrs.dayFontSize
    // 日期原始数据
    private val dateList: List<DateInfo> = DateUtil.buildDateList(monthDate.timeInMillis, viewAttrs.firstDayOfWeek)
    // 星期数目
    private var weekCount = DateUtil.calcWeekCount(dateList)

    // 日期item,包括每项绘制坐标，中心坐标，日期点击范围
    protected lateinit var dateItemList: MutableList<DateItem>
    // 星期信息, key为第几星期，该星期相对当前日历，也即当前月份的第几行；value为该星期(该行)包含日期
    protected lateinit var weekMap: MutableMap<Int, MutableList<DateItem>>

    // 主画笔
    private lateinit var mainPaint: Paint
    // 选中状态画笔
    protected lateinit var selectedPaint: Paint
    // 高亮选中圆半径
    protected var selectedRadius: Float = 0f

    // 可点击的日期
    var clickableDateList: List<DateInfo>? = null
    var unClickableDateList: List<DateInfo>? = null

    // 日历范围
    var minDate: Calendar? = null
    var maxDate: Calendar? = null

    // 上下、左右间距
    var padding: Int = 0

    // 日历点击类型
    var clickableType: ClickableType = ClickableType.NORMAL

    // 日期选中回调
    internal var onDateSelectedListener: OnDateSelectedListener? = null

    override fun onDraw(canvas: Canvas?) {
        initData()
        // 绘制高亮
        drawSelectedBg(canvas)
        // 绘制日期
        drawDays(canvas)
    }

    /**
     * 初始化数据
     */
    private fun initData() {
        if (dataInit) return
        dataInit = true

        // 初始化画笔
        mainPaint = createPaint()
        selectedPaint = createPaint()
        // 计算日期数据
        dateItemList = DateUtil.buildDateItemList(context, dateList, measuredWidth, cellHeight,
            (mainPaint.fontMetrics.bottom + mainPaint.fontMetrics.top).toInt(), padding)
        // 星期数据
        weekMap = DateUtil.buildWeekMap(dateItemList)
        // 计算选中圆半径
        selectedRadius = (dateItemList[0].clickBounds.bottom - dateItemList[0].clickBounds.top) / 2
        // 数据初始化完毕
        afterDataInit()
    }

    /**
     * 数据初始化完毕
     */
    protected open fun afterDataInit() {}

    /**
     * 创建画笔
     */
    private fun createPaint(): Paint = Paint().apply {
        isAntiAlias = true
        isFilterBitmap = true
        textAlign = Paint.Align.CENTER
        textSize = fontSize
    }

    /**
     * 绘制日期
     */
    private fun drawDays(canvas: Canvas?) {
        dateItemList.forEach { drawDay(canvas, it) }
    }

    /**
     * 绘制日期文本
     */
    protected open fun drawDay(canvas: Canvas?, dateItem: DateItem) {
        // 绘制选中效果
        var handleSelectedDate = drawSelectedDay(canvas, dateItem)

        if (!handleSelectedDate) {
            // 设置画笔颜色
            setMainPaintColor(dateItem)
            // 绘制普通文本
            drawDayText(canvas, dateItem, mainPaint)
        }
    }

    /**
     * 设置画笔颜色
     */
    private fun setMainPaintColor(dateItem: DateItem) {
        when (clickableType) {
            ClickableType.CLICKABLE -> { // 可点击日期
                if (clickableDateList?.contains(dateItem.date) == true) {
                    setMonthPaintColor(dateItem)
                } else {
                    setDimPaintColor(dateItem) // 当月没有可点击的
                }
            }
            ClickableType.UN_CLICKABLE -> { // 不可点击日期
                if (unClickableDateList?.contains(dateItem.date) == true) {
                    setDimPaintColor(dateItem)
                } else {
                    setMonthPaintColor(dateItem) // 当月没有不可点击的
                }
            }
            ClickableType.NORMAL -> { // 可点击与不可点都没有设置
                setMonthPaintColor(dateItem)
            }
        }

        // 日子是否超范围
        if (DateUtil.isOutOfRange(minDate, maxDate, monthDate, dateItem.date.day)) {
            setDimPaintColor(dateItem)
        }
    }

    private fun setMonthPaintColor(dateItem: DateItem) {
        // 日期为本月
        if (dateItem.date.type == DateInfo.DateType.CURRENT) {
            setNormalPaintColor(dateItem)
        } else {
            // 非本月日期
            setDimPaintColor(dateItem)
        }
    }

    private fun setNormalPaintColor(dateItem: DateItem) {
        // 是否为周末
        if (DateUtil.isWeekend(dateItem.date)) {
            mainPaint.color = viewAttrs.weekendColor!!
        } else {
            mainPaint.color = viewAttrs.defaultColor!!
        }
    }

    private fun setDimPaintColor(dateItem: DateItem) {
        mainPaint.color = viewAttrs.defaultDimColor!!
    }

    protected open fun setClickablePaintColor(date: DateInfo) {
        // 可点击日期中是否存在该日期，如果存在，则画笔设置高亮
        if (clickableDateList?.contains(date) == true) {
            selectedPaint.color = viewAttrs.selectedDayColor
        } else {
            selectedPaint.color = viewAttrs.selectedDayDimColor
        }
    }

    protected open fun setUnClickablePaintColor(date: DateInfo) {
        // 不可点击日期中是否存在该日期，如果存在，则画笔设置灰色
        if (unClickableDateList?.contains(date) == true) {
            selectedPaint.color = viewAttrs.selectedDayDimColor
        } else {
            selectedPaint.color = viewAttrs.selectedDayColor
        }
    }

    /**
     * 绘制日期文本
     */
    protected fun drawDayText(canvas: Canvas?, dateItem: DateItem, paint: Paint) {
        canvas?.drawText("${dateItem.date.day}", dateItem.drawPoint.x, dateItem.drawPoint.y, paint)
    }

    /**
     * 绘制选中日期
     */
    protected open fun drawSelectedDay(canvas: Canvas?, dateItem: DateItem): Boolean {
        return false
    }

    /**
     * 绘制选中背景
     */
    protected open fun drawSelectedBg(canvas: Canvas?) {}

    /**
     * 选中日期
     */
    private fun onDateSelected(selectedDateItem: DateItem) {
        var changeMonth = false
        var monthPosition = this.positionInCalendar
        var type = selectedDateItem.date.type
        // 选中的日子是否为本月
        if (type != DateInfo.DateType.CURRENT) {
            // 切换月份
            changeMonth = true
            monthPosition = if (type == DateInfo.DateType.PREV) {
                this.positionInCalendar - 1
            } else {
                this.positionInCalendar + 1
            }
        }
        onDateSelected(selectedDateItem, changeMonth, monthPosition)
    }

    /**
     * 选中日期
     */
    protected open fun onDateSelected(selectedDateItem: DateItem, changeMonth: Boolean, monthPosition: Int) {}

    /**
     * 根据选中的日期，更新界面
     */
    open fun updateByDateSelected(isCurrentMonth: Boolean, dateInfo: DateInfo) {}

    /**
     * 点击事件处理
     */
    override fun onTouchEvent(event: MotionEvent?): Boolean {
        when (event?.action) {
            MotionEvent.ACTION_UP -> {
                handleClick(event.x, event.y)
            }
        }
        return true
    }

    /**
     * 处理点击事件
     */
    private fun handleClick(x: Float, y: Float) {
        val selectedDateItem = checkClickPoint(x, y)
        // 判断日期是否可点击
        selectedDateItem?.let { dateItem ->
            // 点击的日子是否已超范围
            if (DateUtil.isOutOfRange(minDate, maxDate, dateItem.date.toCalendar(), dateItem.date.day)) {
                // do nothing
                return
            }
            when (clickableType) {
                ClickableType.CLICKABLE -> { // 可点击日期
                    clickableDateList?.let {
                        if (it.contains(dateItem.date)) {
                            onDateSelected(dateItem)
                        }
                    }
                }
                ClickableType.UN_CLICKABLE -> { // 不可点击日期
                    unClickableDateList?.let {
                        if (!it.contains(dateItem.date)) {
                            onDateSelected(dateItem)
                        }
                    } ?: onDateSelected(dateItem)
                }
                ClickableType.NORMAL -> {  // 可点击与不可点都没有设置
                    onDateSelected(dateItem)
                }
            }
        }
    }

    /**
     * 检查用户点击坐标在哪个日期上
     */
    private fun checkClickPoint(x: Float, y: Float): DateItem? {
        for (item in dateItemList) {
            if (item.clickBounds.contains(x, y)) {
                return item
            }
        }
        return null
    }

    /**
     * 计算选中圆的半径
     */
    private fun calcSelectedRadius(): Float {
        return cellHeight / 2.2f
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)

        val widthSize = MeasureSpec.getSize(widthMeasureSpec)
        val heightSize = MeasureSpec.getSize(heightMeasureSpec)
        val heightMode = MeasureSpec.getMode(heightMeasureSpec)

        var resultWidth = widthSize
        var resultHeight = heightSize
        // 月份高度=星期总行高+上下边距
        cellHeight = ((heightSize - padding * 1.2f) / weekCount).toInt()
        setMeasuredDimension(resultWidth, resultHeight)
    }
}