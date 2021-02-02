package com.androidapp.diaryapp

import io.realm.RealmObject
import org.bson.types.ObjectId

open class TaskRealmObjClass(
    var id: Int? = 0,
    var date_start:String? = null,
    var date_finish:String? = null,
    var name:String? = null,
    var description:String? = null
): RealmObject()