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

import android.view.View
import android.view.ViewGroup
import androidx.viewpager.widget.PagerAdapter
import com.hdev.calendar.base.BaseMonthView
import com.hdev.calendar.constant.Const
import java.util.*

/**
 * 日历适配器
 * Created by han on 2023/4/11.
 */
internal class CalendarAdapter(
    private val minDate: Calendar,
    private val createBlock: (Int, Calendar) -> BaseMonthView
) : PagerAdapter() {

    // 月份个数
    var monthCount = 0

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val currentMonth = Calendar.getInstance()
        currentMonth[Calendar.YEAR] = getYearFromPosition(position)
        currentMonth[Calendar.MONTH] = getMonthFromPosition(position)
        currentMonth[Calendar.DAY_OF_MONTH] = 1
        val monthView = createBlock(position, currentMonth)
        container.addView(monthView)
        return monthView
    }

    override fun destroyItem(container: ViewGroup, position: Int, anyObj: Any) {
        container.removeView(anyObj as View)
    }

    /**
     * 设置日期范围
     */
    fun setRange(min: Calendar, max: Calendar) {
        val minDate = Calendar.getInstance()
        val maxDate = Calendar.getInstance()
        minDate.timeInMillis = min.timeInMillis
        maxDate.timeInMillis = max.timeInMillis
        val diffYear = maxDate[Calendar.YEAR] - minDate[Calendar.YEAR]
        val diffMonth = maxDate[Calendar.MONTH] - minDate[Calendar.MONTH]
        monthCount = diffMonth + Const.MONTHS * diffYear + 1

        notifyDataSetChanged()
    }

    /**
     * 根据position获取月份
     */
    private fun getMonthFromPosition(position: Int): Int {
        return (position + minDate[Calendar.MONTH]) % Const.MONTHS
    }

    /**
     * 根据position获取年份
     */
    private fun getYearFromPosition(position: Int): Int {
        val yearOffset: Int = (position + minDate[Calendar.MONTH]) / Const.MONTHS
        return yearOffset + minDate[Calendar.YEAR]
    }

    override fun isViewFromObject(view: View, anyObj: Any) = view === anyObj

    override fun getCount(): Int {
        return monthCount
    }

    override fun getItemPosition(obj: Any): Int {
        // 强制ViewPager不缓存，每次滑动都刷新视图
        return POSITION_NONE
    }
}