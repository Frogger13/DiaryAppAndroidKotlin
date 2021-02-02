package com.androidapp.diaryapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.PersistableBundle
import android.view.View
import android.widget.CalendarView
import androidx.recyclerview.widget.LinearLayoutManager
import com.androidapp.diaryapp.databinding.ActivityMainBinding
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import io.realm.Realm
import java.text.DateFormat
import java.util.*
import kotlin.collections.ArrayList

class MainActivity : AppCompatActivity() {

    private lateinit var binding: AppCompatActivity
    private lateinit var database: DatabaseReference
    var adapter:MyAdapter?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        Realm.init(this)

        val calendarView = binding.calendarView
        val rcViewTime = binding.rcViewTime
        val selectedDate = calendarView.date

        calendarView.setOnDateChangeListener { view, year, month, dayOfMonth ->

        }

        var list = ArrayList<ListItem>()
        list.addAll(fillArray(resources.getStringArray(R.array.hours),resources.getStringArray(R.array.descriptions)))

        rcViewTime.hasFixedSize()
        rcViewTime.layoutManager = LinearLayoutManager(this)
        adapter = MyAdapter(list,this)
        rcViewTime.adapter = adapter
    }

    fun fillArray(timeArray:Array<String>, descriptionArray:Array<String>):List<ListItem>{
        var listitemArray = ArrayList<ListItem>()
        for(n in 0..timeArray.size-1){
            var listItem = ListItem(timeArray[n], descriptionArray[n])
            listitemArray.add(listItem)
        }
        return listitemArray
    }
    fun onClickAddNewTask(view: View){
        val intent = Intent(this,NewTask::class.java)
        startActivity(intent)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState?.run {
        }
        super.onSaveInstanceState(outState)

    }
}
