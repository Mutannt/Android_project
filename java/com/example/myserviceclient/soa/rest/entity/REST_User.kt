package com.example.myserviceclient.soa.rest

// Имена полей в классе совпадают с именами полей в ответе REST-сервиса. и БД
data class REST_User (
    val ID: Int,
    val Name: String,
    val Age: Int,
    val Mail: String,
    val LanguageID: Int
    )
