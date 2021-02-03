package com.androidapp.diaryapp

 class ListItem {
    var id:Int? = 0
    var time: String = ""
    var name: String = ""
    constructor(id:Int?, time:String, name:String){
        this.id = id
        this.time = time
        this.name = name
    }
    constructor(time:String, name:String){
        this.time = time
        this.name = name
    }
}