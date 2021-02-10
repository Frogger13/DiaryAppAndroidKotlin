package com.androidapp.diaryapp
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.androidapp.diaryapp.databinding.ActivityMainBinding
import com.applandeo.materialcalendarview.CalendarView
import com.applandeo.materialcalendarview.EventDay
import io.realm.Realm
import io.realm.RealmConfiguration
import io.realm.kotlin.where
import java.util.*
import kotlin.collections.ArrayList

class MainActivity: AppCompatActivity() {

    var adapterRcView:AdapterRcView?=null
    var cFCalculator = ComFunCalculator()
    val dbHelper = FirebaseHelper()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        Realm.init(this)
        dbHelper.updateRealmDatabase()

//        val config = RealmConfiguration.Builder().name("realmDB.realm").build()
//        val realm = Realm.getInstance(config)
//        realm.beginTransaction()
//        realm.deleteAll()
//        realm.commitTransaction()

        val calendarView:CalendarView = findViewById(R.id.calendarViewActivityMain)
        val rcViewTime = binding.rcViewTasks
        var selectedDate:String
        calendarView.setOnDayClickListener{ eventDay ->
            selectedDate = eventDay.calendar.timeInMillis.toString()
            adapterRcView!!.updateAdapter(updateArrayListItem(cFCalculator.millisToDate(selectedDate.toLong())))
        }
        val list = ArrayList<ListItem>()
        list.addAll(updateArrayListItem(cFCalculator.getTodayDate()))
        rcViewTime.hasFixedSize()
        rcViewTime.layoutManager = LinearLayoutManager(this)
        adapterRcView = AdapterRcView(list, this)
        rcViewTime.adapter = adapterRcView


//        val events: List<EventDay> = ArrayList()
//        val calendar1 = Calendar.getInstance()
//        calendar1.add(Calendar.DAY_OF_MONTH, 5)
//        events.toMutableList().add(EventDay(calendar1, R.drawable.ic_dot))

//        val events = fillEvents()
//        val events = mutableListOf<EventDay>()
//        val calendar = Calendar.getInstance()
//        calendar.add(Calendar.DAY_OF_MONTH, 5)
//        events.add(EventDay(calendar, R.drawable.ic_dot, Color.parseColor("#228B22")))
//        calendarView.setEvents(events)
    }

//    fun fillArray(timeArray: Array<String>, descriptionArray: Array<String>):List<ListItem>{
//        var listitemArray = ArrayList<ListItem>()
//        for(n in 0..timeArray.size-1){
//            var listItem = ListItem(timeArray[n], descriptionArray[n])
//            listitemArray.add(listItem)
//        }
//        return listitemArray
//    }

    fun updateArrayListItem(selectedDate: String):List<ListItem>{
        val timeArr:Array<String> = resources.getStringArray(R.array.hours)
        val selDateMill = cFCalculator.dateTimeToMillis(selectedDate, null)
        val config = RealmConfiguration.Builder().name("realmDB.realm").build()
        val realm = Realm.getInstance(config)


        val realmResults = realm.where<TaskRealmObjClass>().between("date_start",
            selDateMill!!, (selDateMill + 86400000 - 1)).findAll()

        var nextHourMill:Long
        val realmList = ArrayList<TaskRealmObjClass>()
        realmList.addAll(realmResults.subList(0, realmResults.size))
        val listListItem = List(24) { ListItem(null, "", "") }

        for (i in timeArr.indices){
            listListItem[i].id = null
                listListItem[i].time = timeArr[i]
                listListItem[i].name = ""
        }

        for (j in 0 until realmList.size) {
            nextHourMill = selDateMill
            for (i in 0 until timeArr.size){
                if (realmList[j]?.date_start == nextHourMill) {
                    listListItem[i].id = realmList[j].id!!.toInt()
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

//        for (i in 0 until timeArr.size){
//            if (realmList.size>0) {
//                for (j in 0 until realmList.size) {
//                    if (realmList[j]?.date_start == nextHourMill) {
//                        listListItem[i].id = realmList[j].id!!.toInt()
//                        listListItem[i].time = timeArr[i]
//                        listListItem[i].name = realmList[j].name.toString()
//                        nextHourMill += 3600000
//                        realmList.remove(realmList[j])
//                        break
//                    } else if (listListItem[i].id != null) {
//                        listListItem[i].id = null
//                        listListItem[i].time = timeArr[i]
//                        listListItem[i].name = ""
//                        nextHourMill += 3600000
//                    }
//                }
//            }else{
//                listListItem[i].id = null
//                listListItem[i].time = timeArr[i]
//                listListItem[i].name = ""
//                nextHourMill += 3600000
//            }
//        }
        realm.close()
        return listListItem
    }

    fun fillEvents():ArrayList<EventDay>{
        val config = RealmConfiguration.Builder().name("realmDB.realm").build()
        val realm = Realm.getInstance(config)
        var calendar = Calendar.getInstance()
        val events = ArrayList<EventDay>()
        val results = realm.where(TaskRealmObjClass::class.java).findAll()
        for (i in 0 until results.size){
            calendar.timeInMillis = results[i]?.date_start!!
            calendar.add(Calendar.DAY_OF_MONTH, 0)
            events.add(EventDay(calendar, R.drawable.ic_dot))
        }
        realm.close()
        return events
    }


    fun onClickAddNewTask(view: View){
        val intent = Intent(this, PresenterNewTaskActivity::class.java)
        startActivity(intent)
    }


    override fun onSaveInstanceState(outState: Bundle) {
        outState?.run {
        }
        super.onSaveInstanceState(outState)

    }

}