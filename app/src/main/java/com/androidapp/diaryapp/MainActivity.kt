package com.androidapp.diaryapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.androidapp.diaryapp.databinding.ActivityMainBinding
import com.google.firebase.database.DatabaseReference
import io.realm.Realm
import io.realm.RealmConfiguration
import io.realm.kotlin.where
import kotlin.collections.ArrayList

class MainActivity : AppCompatActivity() {

    private lateinit var binding: AppCompatActivity
    private lateinit var database: DatabaseReference
    var adapterRcView:AdapterRcView?=null
    var selectedDate:String = ""
    var cFCalculator = ComFunCalculator()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        Realm.init(this)

        val calendarView = binding.calendarView
        val rcViewTime = binding.rcViewTime

        calendarView.setOnDateChangeListener { view, year, month, dayOfMonth ->
            selectedDate = "$dayOfMonth-${month + 1}-$year"
            adapterRcView!!.updateAdapter(updateArrayListItem())
        }

        var list = ArrayList<ListItem>()
        list.addAll(fillArray(resources.getStringArray(R.array.hours),resources.getStringArray(R.array.descriptions)))

        rcViewTime.hasFixedSize()
        rcViewTime.layoutManager = LinearLayoutManager(this)
        adapterRcView = AdapterRcView(list,this)
        rcViewTime.adapter = adapterRcView
    }

    fun fillArray(timeArray:Array<String>, descriptionArray:Array<String>):List<ListItem>{
        var listitemArray = ArrayList<ListItem>()
        for(n in 0..timeArray.size-1){
            var listItem = ListItem(timeArray[n], descriptionArray[n])
            listitemArray.add(listItem)
        }
        return listitemArray
    }

    fun updateArrayListItem():List<ListItem>{
        var timeArr:Array<String> = resources.getStringArray(R.array.hours)
        val selDateMill = cFCalculator.dateTimeToMillis(selectedDate, null)
        val config = RealmConfiguration.Builder().name("realmDB.realm").build()
        val realm = Realm.getInstance(config)


        val realmResults = realm.where<TaskRealmObjClass>().between("date_start",
            selDateMill!!, (selDateMill + 86400000 - 1)).findAll()

        var nextHourMill = selDateMill
        val realmList = ArrayList<TaskRealmObjClass>()
        realmList.addAll(realmResults.subList(0,realmResults.size))
        val listListItem = List(24) { ListItem(0, "", "") }



        for (i in 0 until timeArr.size){
            if (realmList.size>0) {
                for (j in 0 until realmList.size) {
                    if (realmList[j]?.date_start == nextHourMill) {
                        listListItem[i].id = realmList[j].id!!.toInt()
                        listListItem[i].time = timeArr[i]
                        listListItem[i].name = realmList[j].name.toString()
                        nextHourMill += 3600000
                        realmList.remove(realmList[j])
                        break
                    } else {
                        listListItem[i].id = null
                        listListItem[i].time = timeArr[i]
                        listListItem[i].name = ""
                        nextHourMill += 3600000
                    }
                }
            }else{
                listListItem[i].id = null
                listListItem[i].time = timeArr[i]
                listListItem[i].name = ""
            }
        }
//        for (j in 0 until realmList.size){
//            timeArr.forEach { time ->
//                if ((realmList[j]?.date_start == nextHourMill)&&(listListItem[]) ){
//                        listListItem[listNum].id = realmList[j]?.id!!.toInt()
//                        listListItem[listNum].time = time
//                        listListItem[listNum].name = realmList[j]?.name.toString()
//                        nextHourMill += 3600000
//                        listNum++
//            }
//                else{
//                    listListItem[listNum].id = null
//                    listListItem[listNum].time = time
//                    listListItem[listNum].name = ""
//                    nextHourMill += 3600000
//                    listNum++
//                }
//            }
//        }
        return listListItem
    }



    fun onClickAddNewTask(view: View){
        val intent = Intent(this,PresenterNewTaskActivity::class.java)
        startActivity(intent)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState?.run {
        }
        super.onSaveInstanceState(outState)

    }
}
