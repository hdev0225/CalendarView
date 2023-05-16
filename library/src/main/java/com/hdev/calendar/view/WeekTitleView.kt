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
import android.graphics.Canvas
import android.graphics.Paint
import android.view.View
import com.hdev.calendar.bean.ViewAttrs
import com.hdev.calendar.constant.Const
import com.hdev.calendar.util.Util

/**
 * 星期标题控件
 * Created by han on 2023/4/6.
 */
class WeekTitleView(
    context: Context,
    var viewAttr: ViewAttrs,
) : View(context) {

    companion object {
        // 星期标题
        private val WEEK_TITLE = listOf("一", "二", "三", "四", "五", "六", "日")
    }

    private var cellWidth: Int = 0
    private val cellHeight: Int = Util.dp2px(context, 38f).toInt()

    private var dataInit = false
    private lateinit var paint: Paint
    private var weekTitles = mutableListOf<String>()

    // 左右间距
    var padding: Int = 0

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        initData()
        drawTitle(canvas)
    }

    /**
     * 初始化数据
     */
    private fun initData() {
        if (dataInit) return
        initPaint()
        initCellWidth()
        initWeekTitle()
        dataInit = true
    }

    private fun initPaint() {
        paint = Paint().apply {
            isAntiAlias = true
            isFilterBitmap = true
            textAlign = Paint.Align.CENTER
            textSize = viewAttr.weekTitleFontSize
        }
    }

    private fun initCellWidth() {
        // 计算格子宽度
        cellWidth = (measuredWidth - padding * 2f).toInt() / Const.COLUMNS_PER_WEEK
    }

    private fun initWeekTitle() {
        viewAttr.weekTitleLabel?.let {
            weekTitles.addAll(it.split("、"))
        }
        if (weekTitles.size != WEEK_TITLE.size) {
            weekTitles.addAll(WEEK_TITLE)
        }
    }

    /**
     * 绘制星期标题
     */
    private fun drawTitle(canvas: Canvas?) {
        paint.color = viewAttr.weekTitleColor
        for (index in weekTitles.indices) {
            val textLeft = cellWidth / 2.0f + index * cellWidth + padding
            val textTop = cellHeight / 2.0f - (paint.fontMetrics.bottom + paint.fontMetrics.top) / 2
            canvas?.drawText(weekTitles[index], textLeft, textTop, paint)
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, MeasureSpec.makeMeasureSpec(cellHeight, MeasureSpec.EXACTLY))
    }
}