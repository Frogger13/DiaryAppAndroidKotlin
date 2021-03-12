package com.androidapp.diaryapp.presentation.MainScreen

import android.content.Context
import android.content.res.Resources
import androidx.appcompat.app.AppCompatActivity
import com.androidapp.diaryapp.R
import com.androidapp.diaryapp.data.FirebaseHelper
import com.androidapp.diaryapp.logic.ComFunCalculator
import com.androidapp.diaryapp.models.ListItemModel
import com.androidapp.diaryapp.models.TaskRealmModel
import io.realm.Realm
import io.realm.RealmConfiguration
import io.realm.kotlin.where

class MainPresenter {

    private final var view:MainView
    private final var cFCalculator:ComFunCalculator
    private final var firebaseHelper:FirebaseHelper

    constructor(view: MainView, cFCalculator: ComFunCalculator, firebaseHelper:FirebaseHelper) {
        this.view = view
        this.cFCalculator = cFCalculator
        this.firebaseHelper = firebaseHelper
    }


    fun updateArrayListItem(context:Context, selectedDate: String):List<ListItemModel>{
        val timeArr:Array<String> =  context.resources.getStringArray(R.array.hours)//resources.getStringArray(R.array.hours)
        val selDateMill = cFCalculator.dateTimeToMillis(selectedDate, null)
        val config = RealmConfiguration.Builder().name("database.realm").build()
        val realm = Realm.getInstance(config)

        firebaseHelper.updateRealmDatabase()
        firebaseHelper.isNetworkConnected(context)

        val realmResults = realm.where<TaskRealmModel>().between("date_start",
            selDateMill!!, (selDateMill + 86400000 - 1)).findAll()

        var nextHourMill:Long
        val realmList = ArrayList<TaskRealmModel>()
        realmList.addAll(realmResults.subList(0, realmResults.size))
        val listListItem = List(24) { ListItemModel(null, "", "") }

        for (i in timeArr.indices){
            listListItem[i].id = null
            listListItem[i].time = timeArr[i]
            listListItem[i].name = ""
        }

        for (j in 0 until realmList.size) {
            nextHourMill = selDateMill
            for (i in timeArr.indices){
                if (realmList[j].date_start == nextHourMill) {
                    listListItem[i].id = realmList[j].id
                    listListItem[i].time = timeArr[i]
                    listListItem[i].name = realmList[j].name.toString()
                    nextHourMill += 3600000
                    break
                } else{
                    nextHourMill += 3600000
                    continue
                }
            }
        }
        realm.close()
        return listListItem
    }
}