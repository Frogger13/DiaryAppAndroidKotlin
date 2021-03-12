package com.androidapp.diaryapp.presentation.AboutTaskScreen

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.androidapp.diaryapp.data.FirebaseHelper
import com.androidapp.diaryapp.presentation.MainScreen.MainActivity
import com.androidapp.diaryapp.R
import com.androidapp.diaryapp.models.TaskRealmModel
import io.realm.Realm
import io.realm.RealmConfiguration


class PresenterAboutTaskActivity:AppCompatActivity() {
    var firebaseHelper = FirebaseHelper()
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
        val config = RealmConfiguration.Builder().name("database.realm").build()
        val realm = Realm.getInstance(config)
        val task = realm.where(TaskRealmModel::class.java).equalTo("id", id).findFirst()
        tvTaskDescriprion.text = task?.description
        realm.close()
    }
    fun onClickDeleteTask(view: View){
        val id = intent.getIntExtra("id", 0)
        firebaseHelper.isNetworkConnected(this)
        firebaseHelper.deleteTaskFromDatabase(id)
        startActivity(Intent(this, MainActivity::class.java))
    }
}