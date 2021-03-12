package com.androidapp.diaryapp

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.androidapp.diaryapp.logic.ComFunCalculator
import com.androidapp.diaryapp.models.TaskRealmModel
import io.realm.Realm
import io.realm.RealmConfiguration
import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class DiaryAppTestnig {
    val context = ApplicationProvider.getApplicationContext<Context>()
    val comFunCalculator = ComFunCalculator()
    @Test
    fun testGiveIdNullDatabase(){
        Realm.init(context)
        val config = RealmConfiguration.Builder().name("test.realm").build()
        val realm = Realm.getInstance(config)
        realm.beginTransaction()
        realm.deleteAll()
        realm.commitTransaction()

        val result = comFunCalculator.giveId(realm)

        realm.beginTransaction()
        realm.deleteAll()
        realm.commitTransaction()

        realm.close()
        Assert.assertEquals(result, 0)
    }

    @Test
    fun testGiveIdDatabaseOneObject(){

        Realm.init(context)
        val config = RealmConfiguration.Builder().name("test.realm").build()
        val realm = Realm.getInstance(config)
        realm.beginTransaction()
        realm.deleteAll()
        realm.copyToRealm(TaskRealmModel(0, 0, 0, "", ""))
        realm.commitTransaction()


        val result = comFunCalculator.giveId(realm)

        realm.beginTransaction()
        realm.deleteAll()
        realm.commitTransaction()

        realm.close()
        Assert.assertEquals(result, 1)
    }

    @Test
    fun testSplitTask(){
        val timeString = "13:00 - 14:00"
        val expectedResult = listOf("13:00", "14:00")
        val result = comFunCalculator.splitTaskTime(timeString)
        Assert.assertEquals(result, expectedResult)
    }
    @Test
    fun testDateTimeToMillis(){
        val expectedResult:Long = 1609506000000
        val result = comFunCalculator.dateTimeToMillis("01-01-2021", "13:00")
        Assert.assertEquals(result, expectedResult)
    }
    @Test
    fun testMillisToDate(){
        val expectedResult = "01-01-2021"
        val result = comFunCalculator.millisToDate(1609459200000)
        Assert.assertEquals(result, expectedResult)
    }
//    @Test
//    fun testCreateNewRealmObjGetId(){
//        val presenter = PresenterNewTaskActivity()
//
//        Realm.init(context)
//        val config = RealmConfiguration.Builder().name("test.realm").build()
//        val realm = Realm.getInstance(config)
//        realm.beginTransaction()
//        realm.deleteAll()
//        realm.commitTransaction()
//
//        val resultId = presenter.createNewRealmObjGetId(realm)
//        val resulRealmSize = realm.where(TaskRealmObjClass::class.java).findAll().size
//        val result = listOf(resultId, resulRealmSize)
//
//        Assert.assertEquals(result, listOf(0, 1))
//    }
}