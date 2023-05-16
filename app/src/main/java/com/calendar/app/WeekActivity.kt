package com.calendar.app

import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.hdev.calendar.bean.DateInfo
import com.hdev.calendar.view.WeekCalendarView

/**
 * 按星期选择
 * Created by han on 2023/4/6.
 */
class WeekActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_week)

        val dateList = mutableListOf<DateInfo>()
        dateList.add(DateInfo(2023, 4, 8))
        dateList.add(DateInfo(2023, 4, 11))
        dateList.add(DateInfo(2023, 3, 31))

        var startDate = DateInfo(2021, 12, 30)
        val endDate = DateInfo(2023, 4, 15)

        // selected date 2023-1-11
        val selectedDate = DateInfo(2022, 1, 1)

        val calendarView: WeekCalendarView = findViewById(R.id.calendar_view)
        // setup un-clickable date
        calendarView.unClickableDateList = dateList

        // setup clickable date
//        calendarView.clickableDateList = dateList
        calendarView.setOnDateRangeSelectedListener {
            _, selecteDate, startDate, endDate ->
                Toast.makeText(this@WeekActivity, "start: $startDate, end:$endDate", Toast.LENGTH_LONG).show()
        }

        // 初始化，设置日期范围
        calendarView.setDateRange(
            startDate.timeInMillis(),
            endDate.timeInMillis(),
            selectedDate.timeInMillis()
        )

        val changeBtn = findViewById<Button>(R.id.change)
        changeBtn.setOnClickListener {
            val d = DateInfo(2022, 5, 25)
            // 选中某一天
            calendarView.setSelectedDate(d)
        }
    }
}