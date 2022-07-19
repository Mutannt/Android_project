package com.example.myserviceclient.soa.rest

import kotlinx.coroutines.*
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.coroutineContext

class RestWrapper: CoroutineScope {

    // Поддержка корутин
    private val rootJob = Job()
    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + rootJob

    private lateinit var service: UserAPI
    private lateinit var service2: LanguageAPI

    public fun Init(){
        val retrofit = Retrofit.Builder().baseUrl("http://10.0.2.2:80/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        service = retrofit.create(UserAPI::class.java)
    }

    public fun Init2(){
        val retrofit = Retrofit.Builder().baseUrl("http://10.0.2.2:80/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        service2 = retrofit.create(LanguageAPI::class.java)
    }

    public fun CreateUser(user:REST_User) = runBlocking {
        withContext(Dispatchers.IO){
            service.CreateUser(user).execute()
        }
    }

    public fun CreateLanguage(language:REST_Language) = runBlocking {
        withContext(Dispatchers.IO){
            service2.CreateLanguage(language).execute()
        }
    }

    public fun ListUsers():List<REST_User>? = runBlocking (){
        withContext(Dispatchers.IO){
            service.ListUsers().execute().body()
        }
    }

    public fun ListLanguages():List<REST_Language>? = runBlocking (){
        withContext(Dispatchers.IO){
            service2.ListLanguages().execute().body()
        }
    }

    public fun ReadUser(id:Int):REST_User? = runBlocking(){
        withContext(Dispatchers.IO){
            service.ReadUser(id).execute().body()
        }
    }

    public fun ReadLanguage(id:Int):REST_Language? = runBlocking(){
        withContext(Dispatchers.IO){
            service2.ReadLanguage(id).execute().body()
        }
    }

    public fun UpdateUser(id:Int, person: REST_User) = runBlocking(){
        withContext(Dispatchers.IO){
            service.UpdateUser(id, person).execute()
        }
    }

    public fun UpdateLanguage(id:Int, language: REST_Language) = runBlocking(){
        withContext(Dispatchers.IO){
            service2.UpdateLanguage(id, language).execute()
        }
    }

    public fun DeleteUser(id:Int) = runBlocking(){
        withContext(Dispatchers.IO){
            service.DeleteUser(id).execute()
        }
    }

    public fun DeleteLanguage(id:Int) = runBlocking(){
        withContext(Dispatchers.IO){
            service2.DeleteLanguage(id).execute()
        }
    }

}