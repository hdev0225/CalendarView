package com.calendar.app

import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.hdev.calendar.base.BaseCalendarView
import com.hdev.calendar.bean.DateInfo
import com.hdev.calendar.view.RangeCalendarView

/**
 * 范围选择
 * Created by han on 2023/4/12.
 */
class RangeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_range)

        val dateList = mutableListOf<DateInfo>()
        dateList.add(DateInfo(2023, 5, 8))
        dateList.add(DateInfo(2023, 5, 11))
        dateList.add(DateInfo(2023, 3, 31))

        val calendarView: RangeCalendarView = findViewById(R.id.calendar_view)
        calendarView.setSelectedDateRange(DateInfo(2023, 2, 21),
            DateInfo(2023, 5, 13))
        calendarView.unClickableDateList = dateList

        calendarView.setOnDateRangeSelectedListener {
                _, // 日历控件
                _, // 选中的日期
                startDate: DateInfo, // 开始日期
                endDate: DateInfo -> // 结束日期
            Toast.makeText(this@RangeActivity, "${startDate.format()}---${endDate.format()}", Toast.LENGTH_SHORT).show()
        }

        val changeBtn = findViewById<Button>(R.id.set_date_range)
        changeBtn.setOnClickListener {
            calendarView.setSelectedDateRange(DateInfo(2023, 1, 20),
                DateInfo(2023, 5, 10))
        }
    }
}