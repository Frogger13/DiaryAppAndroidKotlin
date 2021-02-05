package com.androidapp.diaryapp

import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import io.realm.Realm
import io.realm.RealmConfiguration

class FirebaseHelper {
    fun saveNewTaskToDatabase(id:Int?){
        val database = FirebaseDatabase.getInstance().getReference("Tasks")
//        FirebaseDatabase.getInstance()
//        val database = Firebase.database
//        val ref = database.getReference("Tasks")
        val config = RealmConfiguration.Builder().name("realmDB.realm").build()
        val realm = Realm.getInstance(config)
        val taskRealm = realm.copyFromRealm(realm.where(TaskRealmObjClass::class.java).equalTo("id", id).findFirst())
        val task = TaskKotlinClass(taskRealm?.id, taskRealm?.date_start.toString(), taskRealm?.date_finish.toString(), taskRealm?.name, taskRealm?.description)
        database.setValue(task)
    }

}