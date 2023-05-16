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
import android.view.View
import android.view.View.OnClickListener
import android.widget.TextView
import com.hdev.calendar.R
import com.hdev.calendar.base.BaseHeaderView

/**
 * 默认头部
 * Created by han on 2023/4/10.
 */
open class DefaultHeaderView(
    context: Context
) : BaseHeaderView(context), OnClickListener {

    private lateinit var titleLabel: TextView
    private lateinit var prevMonth: TextView
    private lateinit var nextMonth: TextView

    override fun getLayoutId(): Int {
        return R.layout.default_header_view
    }

    /**
     * 实始化
     */
    override fun init() {
        titleLabel = findViewById(R.id.title_label)
        prevMonth = findViewById(R.id.prev_month)
        nextMonth = findViewById(R.id.next_month)

        prevMonth.setOnClickListener(this)
        nextMonth.setOnClickListener(this)
    }

    /**
     * 更新标题
     */
    override fun updateTitle(year: Int, month: Int) {
        var yearMonth = "${year}年${month}月"
        titleLabel.text = yearMonth
    }

    /**
     * 处理上、下按钮
     */
    override fun handlePrevNext(hasPrev: Boolean, hasNext: Boolean) {
        if (hasPrev) {
            prevMonth.visibility = View.VISIBLE
        } else {
            prevMonth.visibility = View.INVISIBLE
        }
        if (hasNext) {
            nextMonth.visibility = View.VISIBLE
        } else {
            nextMonth.visibility = View.INVISIBLE
        }
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.prev_month -> {
                listener?.prevMonth()
            }
            R.id.next_month -> {
                listener?.nextMonth()
            }
        }
    }
}