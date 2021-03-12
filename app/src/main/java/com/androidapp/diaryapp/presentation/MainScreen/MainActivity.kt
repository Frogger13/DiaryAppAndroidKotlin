package com.androidapp.diaryapp.presentation.MainScreen
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.androidapp.diaryapp.*
import com.androidapp.diaryapp.data.FirebaseHelper
import com.androidapp.diaryapp.presentation.NewTaskScreen.PresenterNewTaskActivity
import com.androidapp.diaryapp.databinding.ActivityMainBinding
import com.androidapp.diaryapp.logic.ComFunCalculator
import com.androidapp.diaryapp.models.ListItemModel
import com.applandeo.materialcalendarview.CalendarView
import io.realm.Realm
import kotlin.collections.ArrayList

class MainActivity: AppCompatActivity(), MainView {

    lateinit var mainPresenter:MainPresenter
    lateinit var adapterRcView: AdapterRcView

    var cFCalculator = ComFunCalculator()
    var firebaseHelper = FirebaseHelper()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Realm.init(this)
        mainPresenter = MainPresenter(this, cFCalculator, firebaseHelper)
        init()
    }

    override fun onDestroy() {
        super.onDestroy()
    }

    fun init(){
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val calendarView: CalendarView = findViewById(R.id.calendarViewActivityMain)
        val rcViewTime = binding.rcViewTasks
        var selectedDate: String

        calendarView.setOnDayClickListener { eventDay ->
            selectedDate = eventDay.calendar.timeInMillis.toString()
            adapterRcView.updateAdapter(mainPresenter.updateArrayListItem(this, cFCalculator.millisToDate(selectedDate.toLong())))
        }

        val list = ArrayList<ListItemModel>()
        val listItem = cFCalculator.getTodayDate()
        list.addAll(mainPresenter.updateArrayListItem(this, listItem))

        rcViewTime.hasFixedSize()
        rcViewTime.layoutManager = LinearLayoutManager(this)
        adapterRcView = AdapterRcView(list, this)
        rcViewTime.adapter = adapterRcView
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