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
 * 范围日历view自定义属性
 * Created by han on 2023/5/18.
 */
class RangeViewAttrs() : ViewAttrs() {

    // 选中区间背景色，开始与结束之间的日期
    var selectedRangeBgColor = 0

    // 选中区间字体颜色，开始与结束之间的日期
    var selectedRangeDayColor = 0

    // 开始日期背景色
    var selectedStartBgColor = 0

    // 开始日期字体颜色
    var selectedStartDayColor = 0

    // 结束日期背景色
    var selectedEndBgColor = 0

    // 结束日期字体颜色
    var selectedEndDayColor = 0
}