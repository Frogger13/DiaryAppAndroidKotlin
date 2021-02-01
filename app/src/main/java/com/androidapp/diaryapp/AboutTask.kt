package com.androidapp.diaryapp

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity


class AboutTask:AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_about_task)

        var tvTaskTime = findViewById<TextView>(R.id.tvTaskTime)
        var tvTaskName = findViewById<TextView>(R.id.tvTaskName)
        tvTaskTime.text = intent.getStringExtra("Time")
        tvTaskName.text = intent.getStringExtra("Name")
    }
}