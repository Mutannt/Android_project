package com.example.myserviceclient.soa.rest
import androidx.room.Update
import retrofit2.Call
import retrofit2.http.*

// интерфейс прокси-класса, методы которого связаны с методами
// REST-сервиса посредством специальных аннотаций:

interface UserAPI{
    @PUT("rest/person")
    fun CreateUser(@Body person: REST_User): Call<Void>

    @GET("rest/people")
    fun ListUsers(): Call<List<REST_User>>

    @GET("rest/person")
    fun ReadUser(@Query("id") id:Int): Call<REST_User>

    @PATCH("rest/person")
    fun UpdateUser(@Query("id")id: Int, @Body person: REST_User): Call<Void>

    @DELETE("rest/person")
    fun DeleteUser(@Query("id")id: Int): Call<Void>
}