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
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.FrameLayout
import com.hdev.calendar.constant.Const

/**
 * Created by han on 2023/4/6.
 */
abstract class BaseHeaderView(
    context: Context,
    attrs: AttributeSet?
) : FrameLayout(context, attrs) {

    // 左右间距
    private var padding: Int = 0
    lateinit var rootView: ViewGroup

    var listener: HeaderViewListener? = null

    constructor(context: Context): this(context, null)

    init {
        initView()
    }

    private fun initView() {
        rootView = LayoutInflater.from(context).inflate(getLayoutId(),
            this, false) as ViewGroup
        addView(rootView)

        init()
    }

    /**
     * 初始化
     */
    protected abstract fun init()

    /**
     * 获取布局id，如：R.layout.default_header_view
     */
    protected abstract fun getLayoutId(): Int

    /**
     * 更新标题
     */
    open fun updateTitle(year: Int, month: Int) {}

    /**
     * 处理上、下按钮
     * @param hasPrev 是否有上一页
     * @param hasNext 是否有下一页
     */
    open fun handlePrevNext(hasPrev: Boolean, hasNext: Boolean) {}

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)
        if (changed) {
            setPadding()
        }
    }

    /**
     * 设置左右间距
     */
    open fun setPadding() {
        // 每一格子大小除以2，取得第一格x轴中心位置
        padding = measuredWidth / Const.COLUMNS_PER_WEEK / 2
        rootView.setPadding(padding, 0, padding, 0)
    }

    interface HeaderViewListener {

        /**
         * 上下个月
         */
        fun prevMonth()

        /**
         * 下一个月
         */
        fun nextMonth()
    }
}