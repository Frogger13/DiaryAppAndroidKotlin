package com.androidapp.diaryapp


import android.os.Bundle
import android.view.View
import com.applandeo.materialcalendarview.CalendarView
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.androidapp.diaryapp.databinding.ActivityAddNewTaskBinding
import com.fasterxml.jackson.databind.ObjectMapper
import com.google.gson.Gson
import io.realm.Realm
import io.realm.RealmConfiguration
import io.realm.kotlin.createObject
import io.realm.kotlin.where


class PresenterNewTaskActivity: AppCompatActivity() {
    private lateinit var binding: AppCompatActivity

    lateinit var etTaskName:EditText
    lateinit var sTaskTime:Spinner
    lateinit var etTaskDescription:EditText
    var selectedDate:String = ""
    var cFCalculator = ComFunCalculator()
    var firebaseHelper = FirebaseHelper()





    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = ActivityAddNewTaskBinding.inflate(layoutInflater)
        setContentView(binding.root)

        Realm.init(this)


        etTaskName = binding.etNewTaskName
        sTaskTime = binding.sNewTaskTime
        etTaskDescription = binding.etNewTaskDescription
        var calendarViewNewTask:CalendarView = findViewById(R.id.calendarViewNewTask)

        calendarViewNewTask.setOnDayClickListener{eventDay ->
            val date = eventDay.calendar.timeInMillis.toString()
            selectedDate = cFCalculator.millisToDate(date.toLong())
        }
//        calendarViewNewTask.setOnDateChangeListener { view, year, month, dayOfMonth ->
//            selectedDate = "$dayOfMonth-${month + 1}-$year"
//        }

    }
    fun onClickSaveNewTask(view: View){
        if(selectedDate != "") {
            val savedRealmTaskId = createNewRealmObjGetId()
            val taskString:String
            if (savedRealmTaskId!=null){
                firebaseHelper.saveNewTaskToDatabase(savedRealmTaskId)
                taskString = cFCalculator.realmToJson(savedRealmTaskId).toString()
                println(taskString)
            }

        }
        else{
            Toast.makeText(this,"Выберите дату", Toast.LENGTH_SHORT).show()
        }
//        Toast.makeText(this, , Toast.LENGTH_SHORT).show()
    }

    private fun createNewRealmObjGetId():Int?{
        val config = RealmConfiguration.Builder().name("realmDB.realm").build()
        val realm = Realm.getInstance(config)

        realm.beginTransaction()
        val taskObject = realm.createObject<TaskRealmObjClass>()
        realm.commitTransaction()

        val taskId = cFCalculator.giveId(realm)
        val taskName = etTaskName.text.toString().trim()
        val taskTime = sTaskTime.selectedItem.toString().trim()
        val splitedHourList = cFCalculator.splitTaskTime(taskTime)
        val taskTimeStart = cFCalculator.dateTimeToMillis(selectedDate, splitedHourList[0])
        val taskTimeFinish:Long
        if (taskTime=="23:00 - 00:00"){
            taskTimeFinish = cFCalculator.dateTimeToMillis(selectedDate, splitedHourList[1])!! + 86400000
        }
        else{
            taskTimeFinish = cFCalculator.dateTimeToMillis(selectedDate, splitedHourList[1])!!
        }

        val taskDescription = etTaskDescription.text.toString().trim()

        if(taskName.isEmpty()){
            etTaskName.error = "Введите название дела"
            return null
        }


        val realmFindResult = realm.where<TaskRealmObjClass>().equalTo("date_start",
            taskTimeStart).findAll()
        realm.beginTransaction()
        if (realmFindResult.size==0){
            taskObject.id = taskId
            taskObject.date_start = taskTimeStart
            taskObject.date_finish = taskTimeFinish
            taskObject.name = taskName
            taskObject.description = taskDescription
            realm.copyFromRealm(taskObject)
        }else{
            realmFindResult.deleteAllFromRealm()
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




//        val task = TaskKotlinClass(0, taskTimeStart, taskTimeFinish, taskName, taskDescription)
//        val jsonString = GsonBuilder().setPrettyPrinting().create().toJson(task.toJSON())


//        val jsonString = ObjectMapper().writeValueAsString(task)
//        val imputStream = assets.open(dataFileName)
//        val gson = Gson()
//        try{
//            val fileOutputStream:FileOutputStream = context.openFileOutput(dataFileName)
//        }
//        val jsonList = gson.toJson(jsonString)
//        File("Database.json").writeText(jsonList)


//        val gsonPretty = GsonBuilder().setPrettyPrinting().create()
//        val jsonStringPretty:String = gsonPretty.toJson(jsonObjTask)
//        jsonWriter(jsonStringPretty)

    }


//    private fun createRealmObj(id:Int, date_start:String, date_finish:String, name:String, description:String){
//        val realm = Realm.getInstance(config)
//        val task = TaskRealmObjClass(id, date_start, date_finish, name, description)
//        realm.copyToRealm(task)
//        realm.close()
//    }



    override fun onSaveInstanceState(outState: Bundle) {
        outState?.run {
        }
        super.onSaveInstanceState(outState)
    }
}