package com.androidapp.diaryapp

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.PersistableBundle
import android.view.View
import android.widget.CalendarView
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.androidapp.diaryapp.databinding.ActivityAddNewTaskBinding
import org.json.JSONArray
import java.text.SimpleDateFormat
import java.util.*
import com.google.gson.GsonBuilder
import java.io.*


class NewTask: AppCompatActivity() {
    private lateinit var binding: AppCompatActivity

    lateinit var etTaskName:EditText
    lateinit var sTaskTime:Spinner
    lateinit var etTaskDescription:EditText
    lateinit var calendarViewNewTask: CalendarView
    var selectedDate:String = ""
    var dataFileName = "raw/data.json"



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityAddNewTaskBinding.inflate(layoutInflater)
        setContentView(binding.root)

        etTaskName = binding.etNewTaskName
        sTaskTime = binding.sNewTaskTime
        etTaskDescription = binding.etNewTaskDescription
        calendarViewNewTask = binding.calendarViewNewTask

        calendarViewNewTask.setOnDateChangeListener { view, year, month, dayOfMonth ->
            selectedDate = "$dayOfMonth-${month + 1}-$year"
        }

    }
    fun onClickSaveNewTask(view: View){
        if(selectedDate!="") {
            saveTask()
        }
        else{
            Toast.makeText(this,"Выберите дату", Toast.LENGTH_SHORT).show()
        }
//        Toast.makeText(this, , Toast.LENGTH_SHORT).show()
    }

    @Suppress("DEPRECATION")
    private fun saveTask(){

        val taskId = giveId(getJsonString())
        val taskName = etTaskName.text.toString().trim()
        val taskTime = sTaskTime.selectedItem.toString().trim()
        val taskTimeStart = dateTimeToMillis(selectedDate, splitTaskTime(taskTime)[0]).toString()
        val taskTimeFinish = dateTimeToMillis(selectedDate, splitTaskTime(taskTime)[1]).toString()
        val taskDescription = etTaskDescription.text.toString().trim()
        if(taskName.isEmpty()){
            etTaskName.error = "Введите название дела"
            return
        }
        val task = Task(taskId, taskTimeStart, taskTimeFinish , taskName, taskDescription)
        val jsonObjTask = task.toJSON()

//        val jsonString = ObjectMapper().writeValueAsString(task)
//        val imputStream = assets.open(dataFileName)
//        val gson = Gson()
//        try{
//            val fileOutputStream:FileOutputStream = context.openFileOutput(dataFileName)
//        }
//        val jsonList = gson.toJson(jsonString)
//        File("Database.json").writeText(jsonList)


        val gsonPretty = GsonBuilder().setPrettyPrinting().create()
        val jsonStringPretty:String = gsonPretty.toJson(jsonObjTask)
        jsonWriter(jsonStringPretty)


    }
    private fun giveId(jsonString: String):Int{
        try {
            val jsonArr = JSONArray(jsonString)
            var maxId = 0
            var objId: Int

            for (i in 0 until jsonArr.length()) {
                objId = jsonArr.getJSONObject(i).getInt("id")
                if (objId > maxId) maxId = objId
            }
            return maxId + 1
        }
        catch (e:IOException){
            return 0
        }
    }


    private fun getJsonString():String {


//        var json = String()
//            val  inputStream:InputStream = assets.open(dataFileName)
//            json = inputStream.bufferedReader().use{it.readText()}
//        return json


//        val file = File(context.filesDir, dataFileName)
//        val fileReader = FileReader(file)
//        val bufferedReader = BufferedReader(fileReader)
//        val stringBuilder = StringBuilder()
//        var line = bufferedReader.readLine()
//        while(line!=null){
//            stringBuilder.append(line).append("\n")
//            line = bufferedReader.readLine()
//        }
//        bufferedReader.close()
//        return stringBuilder.toString()

        val file = R.raw.data
        val reader = this.resources.openRawResource(file).reader()
        val jsonString = reader.readText()
        return jsonString


//        val file = File("src/data.json")
//        println("Attempting to read from file in: "+ Environment.getDataDirectory())
//        try {
//            val reader = file.canonicalFile.reader()
//        }catch (e:IOException){
//            e.printStackTrace()
//        }
//        return ""
    }

    private fun jsonWriter(jsonString:String){
        var fileOutput = openFileOutput(dataFileName, MODE_PRIVATE)
        val outputWriter = OutputStreamWriter(fileOutput)
        outputWriter.write(jsonString)
        outputWriter.close()
    }

    private fun splitTaskTime(rangeString: String): List<String> {
        rangeString.replace(" ", "")
        return rangeString.split("-")
    }

    @Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
    @SuppressLint("SimpleDateFormat")
    private fun dateTimeToMillis(dateString:String, timeString:String):Long? {
        val givenDateString = "$dateString $timeString"
        val sdf = SimpleDateFormat("dd-MM-yyyy HH:mm")
        var timeMilliseconds:Long
        try{
            val date: Date = sdf.parse(givenDateString)
            timeMilliseconds = date.time
            return timeMilliseconds
        }
        catch (e:IOException){
            e.printStackTrace()
            return null
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState?.run {
        }
        super.onSaveInstanceState(outState)
    }
}