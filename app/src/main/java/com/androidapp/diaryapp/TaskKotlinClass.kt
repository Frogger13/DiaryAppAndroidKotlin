package com.androidapp.diaryapp

import org.json.JSONObject

class TaskKotlinClass{
    var id: Int? = null
    var date_start: Long? = null
    var date_finish: Long? = null
    var name: String? = null
    var description: String? = null

    constructor(id: Int?, date_start: Long?, date_finish: Long?, name: String?, description: String?) {
        this.id = id
        this.date_start = date_start
        this.date_finish = date_finish
        this.name = name
        this.description = description
    }
    constructor(name: String?, date_start: Long?, date_finish: Long?, description: String?){
        this.name = name
        this.date_start = date_start
        this.date_finish = date_finish
        this.description = description
    }
    constructor()
    fun toJSON(): JSONObject {
        val jsonObj = JSONObject()
        jsonObj.put("id", id)
        jsonObj.put("date_start", date_start)
        jsonObj.put("date_finish", date_finish)
        jsonObj.put("name", name)
        jsonObj.put("description", description)
        return jsonObj
    }
    fun toMap(): Map<String, Any?> {
        return mapOf(
            "id" to id,
            "date_start" to date_start,
            "date_finish" to date_finish,
            "name" to name,
            "description" to description
        )
    }

}