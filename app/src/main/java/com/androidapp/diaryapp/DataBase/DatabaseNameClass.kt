package com.androidapp.diaryapp.DataBase

import android.provider.BaseColumns

object DatabaseNameClass:BaseColumns {
    object tasks : BaseColumns {
        const val TABLE_NAME = "tasks"
        const val COLUMN_NAME_TITLE = "title"
        const val COLUMN_NAME_SUBTITLE = "subtitle"
    }

}