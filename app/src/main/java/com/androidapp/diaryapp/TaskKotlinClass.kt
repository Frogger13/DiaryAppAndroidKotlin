package com.androidapp.diaryapp

import org.json.JSONObject

class TaskKotlinClass{
    var id: Int? = null
    var date_start: String? = null
    var date_finish: String? = null
    var name: String? = null
    var description: String? = null

    constructor(id: Int?, date_start: String?, date_finish: String?, name: String?, description: String?) {
        this.id = id
        this.date_start = date_start
        this.date_finish = date_finish
        this.name = name
        this.description = description
    }
    constructor(name: String?, date_start: String?, date_finish: String?, description: String?){
        this.name = name
        this.date_start = date_start
        this.date_finish = date_finish
        this.description = description
    }
    fun toJSON(): JSONObject {
        val jsonObj = JSONObject()
        jsonObj.put("id", id)
        jsonObj.put("date_start", date_start)
        jsonObj.put("date_finish", date_finish)
        jsonObj.put("name", name)
        jsonObj.put("description", description)
        return jsonObj
    }

}