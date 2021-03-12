package com.androidapp.diaryapp.models

import io.realm.RealmObject
import org.bson.types.ObjectId

open class TaskRealmModel(
    var id: Int? = 0,
    var date_start:Long? = 0,
    var date_finish:Long? = 0,
    var name:String? = null,
    var description:String? = null
): RealmObject()