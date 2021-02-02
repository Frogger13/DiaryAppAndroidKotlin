package com.androidapp.diaryapp

import android.annotation.SuppressLint
import android.content.Context
import io.realm.Realm
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

 public open class ComFunCalculator {

     open fun giveId(realm: Realm):Int{
         if(!realm.isEmpty) {
             val unique = realm.where(TaskRealmObjClass::class.java).distinct("id").findAll()
             return unique.size + 1
         }else{
             return 0
         }
     }

    open fun getJsonString(context:Context):String {


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
        val reader = context.resources.openRawResource(file).reader()
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

//    private fun jsonWriter(jsonString:String){
//        var fileOutput = openFileOutput(dataFileName, MODE_PRIVATE)
//        val outputWriter = OutputStreamWriter(fileOutput)
//        outputWriter.write(jsonString)
//        outputWriter.close()
//    }

    open fun splitTaskTime(rangeString: String): List<String> {
        rangeString.replace(" ", "")
        return rangeString.split("-")
    }

    @Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
    @SuppressLint("SimpleDateFormat")
    open fun dateTimeToMillis(dateString:String, timeString:String):Long? {
        val givenDateString = "$dateString $timeString"
        val sdf = SimpleDateFormat("dd-MM-yyyy HH:mm")
        var timeMilliseconds:Long
        try{
            val date: Date = sdf.parse(givenDateString)
            timeMilliseconds = date.time
            return timeMilliseconds
        }
        catch (e: IOException){
            e.printStackTrace()
            return null
        }
    }

    open fun getRealmObjects(realm: Realm):String?{
        val obj = realm.where(TaskRealmObjClass::class.java).equalTo("id", 3.toInt()).findAll()
        val out = obj.asJSON()
        return out
    }
}