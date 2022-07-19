package com.example.myserviceclient.mydata

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.myserviceclient.mydata.entity.Language

@Database(entities = [User::class, Language::class],version = 2)
abstract class AppDatabase: RoomDatabase() {
    abstract fun userDao():UserDao
    abstract fun languageDao():LanguageDao
}