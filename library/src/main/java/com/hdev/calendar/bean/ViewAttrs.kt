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

/**
 * 控件属性
 * Created by han on 2023/4/6.
 */
open class ViewAttrs {

    // 星期标题颜色
    var weekTitleColor = 0

    // 星期字体大小
    var weekTitleFontSize = 0f

    // 星期标题，以顿号分割，如：一、二、三、四、五、六、日
    var weekTitleLabel: String? = ""

    // 一周的第一天
    var firstDayOfWeek: Int = 0

    // 日期字体大小
    var dayFontSize = 0f

    // 默认文字颜色
    var defaultColor = 0

    // 默认文字灰色
    var defaultDimColor = 0

    // 周未颜色
    var weekendColor = 0

    // 选中背景色
    var selectedBgColor = 0

    // 选中day颜色
    var selectedDayColor = 0

    // 全限定类名； 通过反射创建header view
    var headerView: String? = null

    // 范围选择或者按星期选择时，该日期不能选择，但该日子在范围或者星期之间
    var selectedDayDimColor = 0
}