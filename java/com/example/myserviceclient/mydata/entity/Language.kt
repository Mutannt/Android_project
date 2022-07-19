package com.example.myserviceclient.mydata.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "table_languages")
data class Language(
    @PrimaryKey(autoGenerate = true) var uid: Int?,
    var Name: String
){
    override fun toString(): String {
        return Name
    }
}