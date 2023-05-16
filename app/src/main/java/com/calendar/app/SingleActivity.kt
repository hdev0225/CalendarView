package com.calendar.app

import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.hdev.calendar.bean.DateInfo
import com.hdev.calendar.view.SingleCalendarView

/**
 * 单选
 * Created by han on 2023/4/6.
 */
class SingleActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_single)

        val dateList = mutableListOf<DateInfo>()
        dateList.add(DateInfo(2023, 4, 8))
        dateList.add(DateInfo(2023, 4, 11))
        dateList.add(DateInfo(2023, 3, 31))

        val calendarView: SingleCalendarView = findViewById(R.id.calendar_view)
        calendarView.setOnSingleDateSelectedListener {
                _, dateInfo ->
                    Toast.makeText(this@SingleActivity, dateInfo.toString(), Toast.LENGTH_LONG).show()}
        // 设置不可点击的日期
        calendarView.unClickableDateList = dateList
        //  setup clickable date
//        calendarView.clickableDateList = dateList

        var startDate = DateInfo(2023, 1, 15)
        val endDate = DateInfo(2023, 4, 15)
        // 选中2023-1-1
        val selectedDate = DateInfo(2023, 1, 1)
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