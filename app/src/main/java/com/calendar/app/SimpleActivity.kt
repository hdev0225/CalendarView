package com.calendar.app

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

/**
 * 简单使用
 * Created by han on 2023/4/6.
 */
class SimpleActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_single)
    }
}