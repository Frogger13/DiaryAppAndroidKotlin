package com.androidapp.diaryapp.logic

import android.annotation.SuppressLint
import com.androidapp.diaryapp.models.TaskRealmModel
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import io.realm.Realm
import io.realm.RealmConfiguration
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*
import java.util.Calendar


open class ComFunCalculator {

     open fun giveId(realm: Realm):Int{

         if(!realm.isEmpty) {
             val unique = realm.where(TaskRealmModel::class.java).findAll()
             for (i in 0 until unique.size){
                 if (i==unique[i]?.id){
                     continue
                 }
                 else {
                     return i
                 }
             }
         }else{
             return 0
         }
         val lastId = (realm.where(TaskRealmModel::class.java).findAll()).size
         return lastId
     }


    open fun splitTaskTime(rangeString: String): List<String> {
        val listHours = rangeString.split("-")
        val arrHours: Array<String> = listHours.toTypedArray()
        arrHours[0] = listHours[0].replace(" ", "")
        arrHours[1] = listHours[1].replace(" ", "")
        return arrHours.toList()
    }

    @Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
    @SuppressLint("SimpleDateFormat")
    open fun dateTimeToMillis(dateString: String, timeString: String?):Long? {
        val givenDateString:String
        var sdf: SimpleDateFormat
        val timeMilliseconds: Long
        if (timeString!=null) {
            givenDateString = "$dateString $timeString"
            sdf = SimpleDateFormat("dd-MM-yyyy HH:mm")
            try {
                val date: Date = sdf.parse(givenDateString)
                timeMilliseconds = date.time
                return timeMilliseconds
            } catch (e: IOException) {
                e.printStackTrace()
                return null
            }
        }else {
            sdf = SimpleDateFormat("dd-MM-yyyy")
            try {
                val date: Date = sdf.parse(dateString)
                timeMilliseconds = date.time
                return timeMilliseconds
            } catch (e: IOException) {
                e.printStackTrace()
                return null
            }

        }
    }
     @SuppressLint("SimpleDateFormat")
     open fun millisToDate(millis: Long):String{
         val sdf = SimpleDateFormat("dd-MM-yyyy")
         return sdf.format(Date(millis))
     }

    @SuppressLint("SimpleDateFormat")
    open fun millisToDateHour(millis: Long):String{
        val sdf = SimpleDateFormat("dd-MM-yyyy HH:mm")
        return sdf.format(Date(millis))
    }

     @SuppressLint("SimpleDateFormat")
     open fun getTodayDate():String{
         val date = Calendar.getInstance().time
         val sdf = SimpleDateFormat("dd-MM-yyyy")
         return sdf.format(date)
     }
    open fun realmToJson(id:Int): String? {
        val config = RealmConfiguration.Builder().name("database.realm").build()
        val realm = Realm.getInstance(config)
        val gson = Gson()
        val taskRealm = realm.copyFromRealm(realm.where(TaskRealmModel::class.java).equalTo("id", id).findFirst())
        val json = gson.toJson(taskRealm)
        val gsonPretty = GsonBuilder().setPrettyPrinting().create()
        val jsonStringPretty:String = gsonPretty.toJson(json)
        return jsonStringPretty
    }
}



