package com.example.myserviceclient.soa.rest
import androidx.room.Update
import retrofit2.Call
import retrofit2.http.*

// интерфейс прокси-класса, методы которого связаны с методами
// REST-сервиса посредством специальных аннотаций:

interface LanguageAPI{
    @PUT("rest/language")
    fun CreateLanguage(@Body language: REST_Language): Call<Void>

    @GET("rest/languages")
    fun ListLanguages(): Call<List<REST_Language>>

    @GET("rest/language")
    fun ReadLanguage(@Query("id") id:Int): Call<REST_Language>

    @PATCH("rest/language")
    fun UpdateLanguage(@Query("id")id: Int, @Body language: REST_Language): Call<Void>

    @DELETE("rest/language")
    fun DeleteLanguage(@Query("id")id: Int): Call<Void>
}