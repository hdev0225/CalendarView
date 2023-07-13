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
import android.content.res.TypedArray
import android.graphics.Color
import android.util.AttributeSet
import android.util.TypedValue
import com.hdev.calendar.R
import com.hdev.calendar.base.BaseHeaderView
import com.hdev.calendar.bean.RangeViewAttrs
import com.hdev.calendar.bean.ViewAttrs
import com.hdev.calendar.constant.Const
import com.hdev.calendar.view.DefaultHeaderView

/**
 * Created by han on 2023/4/5.
 */
object Util {

    fun dp2px(context: Context, px: Float) =
        TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, px, context.resources.displayMetrics)

    inline fun <R> notNull(vararg args: Any?, block: () -> R) {
        if (args.filterNotNull().size == args.size) {
            block()
        }
    }

    /**
     * 设置控件通用属性
     */
    fun createViewAttrs(context: Context, attrs: AttributeSet): ViewAttrs {
        val viewAttrs = ViewAttrs()
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.CalendarView)
        setViewAttr(context, viewAttrs, typedArray)
        typedArray.recycle()
        return viewAttrs
    }

    /**
     * 范围控件属性
     */
    fun createRangeViewAttrs(context: Context, attrs: AttributeSet): RangeViewAttrs {
        val viewAttrs = RangeViewAttrs()
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.CalendarView)
        setViewAttr(context, viewAttrs, typedArray)

        viewAttrs.selectedRangeBgColor = typedArray.getColor( R.styleable.CalendarView_selected_range_bg_color,
            Color.parseColor("#800078D7"))
        viewAttrs.selectedRangeDayColor = typedArray.getColor( R.styleable.CalendarView_selected_range_day_color,
            Color.parseColor("#ffffff"))
        viewAttrs.selectedStartBgColor = typedArray.getColor( R.styleable.CalendarView_selected_start_bg_color,
            Color.parseColor("#0037A6"))
        viewAttrs.selectedStartDayColor = typedArray.getColor( R.styleable.CalendarView_selected_start_day_color,
            Color.parseColor("#ffffff"))
        viewAttrs.selectedEndBgColor = typedArray.getColor( R.styleable.CalendarView_selected_end_bg_color,
            Color.parseColor("#0037A6"))
        viewAttrs.selectedEndDayColor = typedArray.getColor( R.styleable.CalendarView_selected_end_day_color,
            Color.parseColor("#ffffff"))
        typedArray.recycle()
        return viewAttrs
    }

    /**
     * 设置控件属性
     */
    private fun setViewAttr(context: Context, viewAttrs: ViewAttrs, typedArray: TypedArray) {
        viewAttrs.weekTitleColor = typedArray.getColor(R.styleable.CalendarView_week_title_color,
            Color.parseColor("#666666"))
        viewAttrs.defaultColor = typedArray.getColor(R.styleable.CalendarView_default_color,
            Color.parseColor("#666666"))
        viewAttrs.defaultDimColor = typedArray.getColor(R.styleable.CalendarView_default_dim_color,
            Color.parseColor("#D3D3D3"))
        viewAttrs.weekendColor = typedArray.getColor(R.styleable.CalendarView_weekend_color,
            Color.parseColor("#3568B9"))
        viewAttrs.selectedBgColor = typedArray.getColor(R.styleable.CalendarView_selected_bg_color,
            Color.parseColor("#0037A6"))
        viewAttrs.selectedDayColor = typedArray.getColor(R.styleable.CalendarView_selected_day_color,
            Color.parseColor("#FFFFFF"))
        viewAttrs.selectedDayDimColor = typedArray.getColor(R.styleable.CalendarView_selected_day_dim_color,
            Color.parseColor("#4DFFFFFF")
        )
        viewAttrs.dayFontSize = typedArray.getDimension(R.styleable.CalendarView_day_font_size,
            dp2px(context, 16f)
        )
        viewAttrs.weekTitleFontSize = typedArray.getDimension(R.styleable.CalendarView_week_title_font_size,
            dp2px(context, 16f)
        )
        viewAttrs.weekTitleLabel = typedArray.getString(R.styleable.CalendarView_week_title_label)
        viewAttrs.headerView = typedArray.getString(R.styleable.CalendarView_header_view)
        viewAttrs.firstDayOfWeek = typedArray.getInt(R.styleable.CalendarView_first_day_of_week, 1);
    }

    /**
     * 创建header view
     */
    fun createHeaderView(context: Context, headerViewName: String?): BaseHeaderView {
        var headerView: BaseHeaderView? = null
        headerViewName?.let {
            try {
                val clazz = Class.forName(it)
                val constructor = clazz.getConstructor(Context::class.java)
                headerView = constructor.newInstance(context) as BaseHeaderView
            } catch (e: Exception) {
                e.printStackTrace()
                throw RuntimeException("header_view必须是com.hdev.calendar.base.BaseHeaderView子类")
            }
        }
        if (headerView == null) {
            headerView = DefaultHeaderView(context)
        }
        return headerView!!
    }

    /**
     * 获取month view tag
     */
    fun getMonthViewTag(position: Int): String {
        return "${Const.VIEW_PREFIX}_${position}"
    }
}