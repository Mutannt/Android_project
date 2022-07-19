package com.example.myserviceclient.soa.rest

import androidx.room.Entity
import androidx.room.PrimaryKey

// Имена полей в классе совпадают с именами полей в ответе REST-сервиса. и БД
data class REST_Language (
    @PrimaryKey(autoGenerate = true) var ID: Int?,
    val Name: String
)
//{
//    override fun toString(): String {
//        return Name
//    }
//}

