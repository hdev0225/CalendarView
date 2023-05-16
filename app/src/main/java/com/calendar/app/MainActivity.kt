package com.calendar.app

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

/**
 * Created by han on 2023/4/6.
 */
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        findViewById<Button>(R.id.simple).setOnClickListener {
            startActivity(Intent(this, SimpleActivity::class.java))
        }
        findViewById<Button>(R.id.select_day).setOnClickListener {
            startActivity(Intent(this, SingleActivity::class.java))
        }
        findViewById<Button>(R.id.select_multi).setOnClickListener {
            startActivity(Intent(this, MultiActivity::class.java))
        }
        findViewById<Button>(R.id.select_week).setOnClickListener {
            startActivity(Intent(this, WeekActivity::class.java))
        }
        findViewById<Button>(R.id.select_range).setOnClickListener {
            startActivity(Intent(this, RangeActivity::class.java))
        }
        findViewById<Button>(R.id.style).setOnClickListener {
            startActivity(Intent(this, StyleActivity::class.java))
        }
    }
}