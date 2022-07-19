package com.example.myserviceclient.mydata

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.example.myserviceclient.mydata.entity.Language

@Entity(tableName = "table_users", foreignKeys = [
    // Настройка связей между таблицами
    ForeignKey(
        // Таблица на которую ссылается внешний ключ
        entity = Language::class,
        // Поле, являющееся внешним ключом
        childColumns = arrayOf("LanguageID"),
        // Поле являющееся первичным ключом во нешненей таблице
        parentColumns = arrayOf("uid"),
        // Задание способа поддержки ссылочной целоствности
        // - в данном случае запрет на удаление языков,
        // на которые ссылается хотя бы один пользователь
        onDelete = ForeignKey.RESTRICT
    )
])
data class User (
    @PrimaryKey(autoGenerate = true) var uid: Int?,
    @ColumnInfo(name = "first_name") var Name: String,
    var Age: Int,
    var Email:String?,
    var LanguageID: Int
)