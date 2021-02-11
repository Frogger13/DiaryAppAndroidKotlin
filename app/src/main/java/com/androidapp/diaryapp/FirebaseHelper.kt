package com.androidapp.diaryapp

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.widget.Toast
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import io.realm.Realm
import io.realm.RealmConfiguration
import io.realm.kotlin.createObject

class FirebaseHelper {
    var database = FirebaseDatabase.getInstance().getReference("Tasks")


    @SuppressLint("RestrictedApi")
    fun saveNewTaskToDatabase(id: Int?) {
        val config = RealmConfiguration.Builder().name("realmDB.realm").build()
        val realm = Realm.getInstance(config)

        val taskRealm =
            realm.copyFromRealm(realm.where(TaskRealmObjClass::class.java).equalTo("id", id)
                .findFirst())
        val task = TaskKotlinClass(taskRealm?.id,
            taskRealm?.date_start,
            taskRealm?.date_finish,
            taskRealm?.name,
            taskRealm?.description)
        var added = false
        database.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    for (h in snapshot.children) {
                        val firebaseTask = h.getValue(TaskKotlinClass::class.java)
                        if (firebaseTask?.id == id) {
                            database.child(h.key.toString()).child("date_start")
                                .setValue(task.date_start?.toLong())
                            database.child(h.key.toString()).child("date_finish")
                                .setValue(task.date_finish?.toLong())
                            database.child(h.key.toString()).child("name").setValue(task.name)
                            database.child(h.key.toString()).child("description")
                                .setValue(task.description)
                            added = true
                        }
                    }
                    if (!added) {
                        database.push().setValue(task)
                    }
                } else {
                    database.push().setValue(task)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                System.err.println("Listener was canceled")
            }
        })
        realm.close()
    }

    fun updateRealmDatabase() {
        database.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val config = RealmConfiguration.Builder().name("realmDB.realm").build()
                val realm = Realm.getInstance(config)
                realm.beginTransaction()
                realm.deleteAll()
                realm.commitTransaction()
                    if (snapshot.exists()) {
                        for (h in snapshot.children) {
                            realm.beginTransaction()
                            val task = h.getValue(TaskKotlinClass::class.java)
                            val taskObject = realm.createObject<TaskRealmObjClass>()
                            taskObject.id = task?.id
                            taskObject.date_start = task?.date_start
                            taskObject.date_finish = task?.date_finish
                            taskObject.name = task?.name
                            taskObject.description = task?.description
                            realm.copyFromRealm(taskObject)
                            realm.commitTransaction()
                        }
                    }
                realm.close()
            }

            override fun onCancelled(error: DatabaseError) {
                System.err.println("Listener was canceled")
            }

        })


    }

    fun deleteTaskFromDatabase(id: Int?) {
        val config = RealmConfiguration.Builder().name("realmDB.realm").build()
        val realm = Realm.getInstance(config)

            database.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        for (h in snapshot.children) {
                            val firebaseTask = h.getValue(TaskKotlinClass::class.java)
                            if (firebaseTask?.id == id) {
                                database.child(h.key.toString()).setValue(null)
                            }
                        }
                    }

                }


                override fun onCancelled(error: DatabaseError) {
                    System.err.println("Listener was canceled")
                }
            })
        realm.beginTransaction()
        realm.where(TaskRealmObjClass::class.java).equalTo("id", id).findFirst()?.deleteFromRealm()
        realm.commitTransaction()
        realm.close()
    }

    @SuppressLint("ShowToast")
    fun isNetworkConnected(context: Context) {
        var isConnected = false
        val connectedRef = Firebase.database.getReference(".info/connected")
        connectedRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val connected = snapshot.getValue(Boolean::class.java) ?: false
                isConnected = connected
                if (!isConnected){
                    Toast.makeText(context, "Network is not connected", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onCancelled(error: DatabaseError) {
                System.err.println("Listener was canceled")
            }
        })

    }
}