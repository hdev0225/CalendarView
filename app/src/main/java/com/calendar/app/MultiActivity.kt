package com.calendar.app

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.hdev.calendar.bean.DateInfo
import com.hdev.calendar.view.MultiCalendarView

/**
 * 多选
 * Created by han on 2023/4/6.
 */
class MultiActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_multi)
        val dateList = mutableListOf<DateInfo>()
        dateList.add(DateInfo(2023, 5, 3))
        dateList.add(DateInfo(2023, 5, 8))
        dateList.add(DateInfo(2023, 5, 5))

        var startDate = DateInfo(2023, 1, 15)
        val endDate = DateInfo(2024, 4, 15)

        // 选中2023-1-1
        val selectedDate = DateInfo(2023, 5, 1)

        val calendarView: MultiCalendarView = findViewById(R.id.calendar_view)
        calendarView.setOnMultiDateSelectedListener {
            _, clickedDate, dateList ->
                Toast.makeText(this@MultiActivity, "clickedDate: $clickedDate", Toast.LENGTH_SHORT).show()
                Toast.makeText(this@MultiActivity, "dateList: $dateList", Toast.LENGTH_SHORT).show()
        }
        calendarView.selectedDateList = dateList

        // 初始化，设置日期范围
        calendarView.setDateRange(
            startDate.timeInMillis(),
            endDate.timeInMillis(),
            selectedDate.timeInMillis()
        )

        findViewById<Button>(R.id.get_dates).setOnClickListener {
            Toast.makeText(this, "${calendarView.selectedDateList}", Toast.LENGTH_SHORT).show()
            Log.i(MultiActivity::class.java.simpleName, "${calendarView.selectedDateList}")
        }
    }
}