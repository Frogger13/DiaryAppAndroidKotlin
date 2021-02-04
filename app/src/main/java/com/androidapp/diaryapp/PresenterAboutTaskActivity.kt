package com.androidapp.diaryapp

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import io.realm.Realm
import io.realm.RealmConfiguration


class PresenterAboutTaskActivity:AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_about_task)

        var tvTaskTime = findViewById<TextView>(R.id.tvTaskTime)
        var tvTaskName = findViewById<TextView>(R.id.tvTaskName)
        val tvTaskDescriprion = findViewById<TextView>(R.id.tvTaskDescription)
        val id = intent.getIntExtra("id", 0)
        tvTaskTime.text = intent.getStringExtra("Time")
        tvTaskName.text = intent.getStringExtra("Name")
        Realm.init(this)
        val config = RealmConfiguration.Builder().name("realmDB.realm").build()
        val realm = Realm.getInstance(config)
        val task = realm.where(TaskRealmObjClass::class.java).equalTo("id", id).findFirst()
        tvTaskDescriprion.text = task?.description
        realm.close()
    }
}