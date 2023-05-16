package com.calendar.app.view

import android.content.Context
import android.view.View
import android.view.View.OnClickListener
import android.widget.ImageView
import android.widget.TextView
import com.calendar.app.R
import com.hdev.calendar.base.BaseHeaderView

/**
 * 自定义头部
 * Created by han on 2023/4/10.
 */
class CustomHeaderView(
    context: Context
) : BaseHeaderView(context), OnClickListener {

    private lateinit var titleLabel: TextView
    private lateinit var prevMonth: ImageView
    private lateinit var nextMonth: ImageView

    override fun getLayoutId(): Int {
        return R.layout.header_view
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