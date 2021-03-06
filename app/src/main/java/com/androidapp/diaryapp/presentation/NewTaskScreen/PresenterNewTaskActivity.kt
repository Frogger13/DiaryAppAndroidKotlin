package com.androidapp.diaryapp.presentation.NewTaskScreen


import android.content.Intent
import android.os.Bundle
import android.view.View
import com.applandeo.materialcalendarview.CalendarView
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.androidapp.diaryapp.logic.ComFunCalculator
import com.androidapp.diaryapp.data.FirebaseHelper
import com.androidapp.diaryapp.presentation.MainScreen.MainActivity
import com.androidapp.diaryapp.R
import com.androidapp.diaryapp.databinding.ActivityAddNewTaskBinding
import com.androidapp.diaryapp.models.TaskRealmModel
import io.realm.Realm
import io.realm.RealmConfiguration
import io.realm.kotlin.createObject
import io.realm.kotlin.where
import java.util.*


class PresenterNewTaskActivity: AppCompatActivity() {

    lateinit var etTaskName:EditText
    lateinit var sTaskTime:Spinner
    lateinit var etTaskDescription:EditText
    var cFCalculator = ComFunCalculator()
    var selectedDate:String = ""
    var firebaseHelper = FirebaseHelper()
    val config = RealmConfiguration.Builder().name("database.realm").build()





    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = ActivityAddNewTaskBinding.inflate(layoutInflater)
        setContentView(binding.root)

        Realm.init(this)


        selectedDate = cFCalculator.millisToDate(Calendar.getInstance().timeInMillis)

        etTaskName = binding.etNewTaskName
        sTaskTime = binding.sNewTaskTime
        etTaskDescription = binding.etNewTaskDescription
        var calendarViewNewTask:CalendarView = findViewById(R.id.calendarViewNewTask)

        calendarViewNewTask.setOnDayClickListener{eventDay ->
            val date = eventDay.calendar.timeInMillis.toString()
            selectedDate = cFCalculator.millisToDate(date.toLong())
        }
    }

    fun onClickSaveNewTask(view: View){
        if(selectedDate != "") {
            val realm = Realm.getInstance(config)
            val savedRealmTaskId = createNewRealmObjGetId(realm)
            if (savedRealmTaskId!=null){
                firebaseHelper.isNetworkConnected(this)
                firebaseHelper.saveNewTaskToDatabase(savedRealmTaskId)
                Toast.makeText(this, "Дело добавлено", Toast.LENGTH_SHORT).show()
                startActivity(Intent(this, MainActivity::class.java))
            }

        }
        else{
            Toast.makeText(this,"Выберите дату", Toast.LENGTH_SHORT).show()
        }
    }

    fun createNewRealmObjGetId(realm:Realm):Int? {
        val taskId: Int
        val taskName = etTaskName.text.toString().trim()
        val taskTime = sTaskTime.selectedItem.toString().trim()
        val splitedHourList = cFCalculator.splitTaskTime(taskTime)
        val taskTimeStart = cFCalculator.dateTimeToMillis(selectedDate, splitedHourList[0])
        val taskTimeFinish: Long
        if (taskTime == "23:00 - 00:00") {
            taskTimeFinish =
                cFCalculator.dateTimeToMillis(selectedDate, splitedHourList[1])!! + 86400000
        } else {
            taskTimeFinish = cFCalculator.dateTimeToMillis(selectedDate, splitedHourList[1])!!
        }

        val taskDescription = etTaskDescription.text.toString().trim()

        if (taskName.isEmpty()) {
            etTaskName.error = "Введите название дела"
            return null
        }


        val realmFindResult = realm.where<TaskRealmModel>().equalTo("date_start",
            taskTimeStart).findAll()
        realm.beginTransaction()
        if (realmFindResult.size == 0) {
            taskId = cFCalculator.giveId(realm)
            val taskObject = realm.createObject<TaskRealmModel>()
            taskObject.id = taskId
            taskObject.date_start = taskTimeStart
            taskObject.date_finish = taskTimeFinish
            taskObject.name = taskName
            taskObject.description = taskDescription
            realm.copyFromRealm(taskObject)
        } else {
            realmFindResult.deleteAllFromRealm()
            taskId = cFCalculator.giveId(realm)
            val taskObject = realm.createObject<TaskRealmModel>()
            taskObject.id = taskId
            taskObject.date_start = taskTimeStart
            taskObject.date_finish = taskTimeFinish
            taskObject.name = taskName
            taskObject.description = taskDescription
            realm.copyFromRealm(taskObject)
        }
        realm.commitTransaction()
        realm.close()

        return taskId


    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState?.run {
        }
        super.onSaveInstanceState(outState)
    }
}