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
import android.widget.TextView
import com.hdev.calendar.R
import com.hdev.calendar.bean.DateInfo
import com.hdev.calendar.listener.IDateRange

/**
 * 包含日期范围的头部
 * Created by han on 2023/5/16.
 */
class DefaultRangeHeaderView(
    context: Context
) : DefaultHeaderView(context), IDateRange {

    private lateinit var dateRangeLabel: TextView

    override fun dateRange(startDate: DateInfo?, endDate: DateInfo?) {
        if (startDate == null && endDate == null) {
            dateRangeLabel.text = ""
        } else if (startDate != null && endDate != null) {
            dateRangeLabel.text = "${startDate.format()} -- ${endDate.format()}"
        } else if (startDate != null) {
            dateRangeLabel.text = startDate.format()
        }
    }

    override fun getLayoutId(): Int {
        return R.layout.default_range_header_view
    }

    override fun init() {
        super.init()
        dateRangeLabel = findViewById(R.id.date_range_label)
    }
}