package com.example.myserviceclient.mydata

import androidx.room.*
import com.example.myserviceclient.mydata.entity.Language

//import org.intellij.lang.annotations.Language

@Dao
interface LanguageDao {
    // CRUD - Create, Read, Update, Delete
    //------------Create--------
    @Insert
    fun insertAll(vararg  language: Language)

    //----------Read-----------

    // все пользователи
    @Query("SELECT * FROM table_languages")
    fun getAll():List<Language>

    // пользователь с заданным id
    @Query("SELECT * FROM table_languages WHERE uid=:Id")
    fun getLanguageById(Id:Int): Language

    //-------Update-------
    @Update
    fun update(language: Language)

    //------Delete--------
    @Delete
    fun delete(language: Language)

    @Query("DELETE FROM table_languages")
    fun deleteAll()
}