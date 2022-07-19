package com.example.myserviceclient

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.room.Room
import java.lang.Exception
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myserviceclient.mydata.AppDatabase

//import com.example.myserviceclient.mydata.User
import com.example.myserviceclient.soa.rest.REST_User
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.example.myserviceclient.soa.rest.RestWrapper



class ActivityUsers : AppCompatActivity() {
    private lateinit var tv_test: TextView
    private lateinit var fab_add:FloatingActionButton
    private lateinit var rv_users: RecyclerView
    private lateinit var adapter: RecyclerViewAdapter_Users
    private lateinit var rv_users_items: ArrayList<REST_User>
    //private lateinit var db:AppDatabase
    private lateinit var my_intent: Intent

    private lateinit var soa_wrapper : RestWrapper


    private var edit_id: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_users)

        // Задать заголовок окна
        supportActionBar!!.title = ("Пользователи")

        // Добавить кнопку "Стрелка обратно"
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        // Создание объекта для доступа к окну "Пользователь"
        my_intent = Intent(this,ActivityUser::class.java)

        // Проверка работы слоя доступа к данным получение доступа
        // к текстовому полю на форме
        tv_test=findViewById(R.id.tv_test)

        // Соединение с базой данных
        try{
//            db = Room.databaseBuilder(
//                applicationContext,
//                AppDatabase::class.java,
//                "my_database"
//            ).allowMainThreadQueries().build()
            soa_wrapper = RestWrapper()
            soa_wrapper.Init()
        }catch (ex: Exception){
            tv_test.setText("Ошибка "+ex.message)
        }


        fab_add = findViewById(R.id.fab_add)

        fab_add.setOnClickListener{
            val my_intent = Intent(this, ActivityUser::class.java)
            // putExtra - передача в открываемое (второе) окно некоторых данных
            my_intent.putExtra("is_edit",0)

            // Откроем окно для создания нового
            startActivityForResult(my_intent,1)
        }

        // Первоначальная настройка RecyclerView
        // получение доступа  \к элементу RecyclerView, расположенному на форме
        rv_users=findViewById(R.id.rv_users)

        // Задание в качестве компоновочного контейнера для элементов списка
        // компонента LinearLayoutManager с вертикальным расположением элементов
        val linearLayoutManager = LinearLayoutManager(applicationContext)
        linearLayoutManager.orientation = LinearLayoutManager.VERTICAL
        rv_users.layoutManager = linearLayoutManager

        // Создание массива элементов, через который в RecyclerView будут поступать
        // элементы списка
        rv_users_items = ArrayList<REST_User>()
        // Создание экземпляра объекта RecyclerViewAdapter_Users,
        // определяющего логику работы элемента RecyclerView
        adapter = RecyclerViewAdapter_Users(rv_users_items, object:RecyclerViewOnClickListenter {
            // Обработчик шелчка на элементе списка
            override fun onClicked(idx: Int) {
                //// Выводим имя выбранного пользователя в заголовке окна
                //supportActionBar!!.title="Вы выбрали: " + rv_users_items[idx-1].Name
                try{
                    // Открытие окна "Пользователь" для редактирования данных
                    // Определяем id пользователя выбранного в списке
                    //edit_id=idx
                    // Извлечения данных выбранного пользователя из БД по его id
                    //val user = db.userDao().getUserById(edit_id)
                    val user = soa_wrapper.ReadUser(idx)// Извлечения данных выбранного пользователя из БД по его id

                    // Передаём открываемому окну флаг is_edit=1
                    // чтобы окно открылось в режиме "Редактирование"
                    my_intent.putExtra("is_edit",1)

                    // Передача в открываемое окно данных редактируемого пользователя
                    //my_intent.putExtra("user_name", user.Name);
                    //my_intent.putExtra("user_age", user.Age.toString());
                    //my_intent.putExtra("user_email", user.Email);
                    //my_intent.putExtra("user_languageid", user.LanguageID);

                    // Работа 3
                    //val user = soa_wrapper.ReadUser(idx)// Извлечения данных выбранного пользователя из БД по его id
                    my_intent.putExtra("user_name",user?.Name);
                    my_intent.putExtra("user_age",user?.Age.toString());
                    my_intent.putExtra("user_email",user?.Mail);
                    my_intent.putExtra("user_languageid",user?.LanguageID);


                    // Открытие окна
                    startActivityForResult(my_intent,2)
                }catch (ex:Exception){
                    // Если при открытие окна произошла ошибка - выведем её
                    tv_test.setText("Ошибка "+ex.message)
                }

            }

        })

        rv_users.adapter = adapter

        // Тестируем работу RecyclerView

//        // Добавляем элементы в массив rv_users_items
//        rv_users_items.add(User(1,"Vasya",50,"vasya@mail.ru"))
//        rv_users_items.add(User(2,"Kolya",25,"kolya@mail.ru"))
//        rv_users_items.add(User(3,"Petya",44,"petya@mail.ru"))
//        rv_users_items.add(User(4,"Vanya",4,"vanya@mail.ru"))
//        // и сообщаем RecyclerView, что данные в массиве изменились
//        // и необходимо выполнить перерисовку списка
//        adapter.notifyDataSetChanged()

        RecyclerViewReloadData()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        // Обработчик завершения дочернего окна

        // Создание
        if(requestCode==1 && resultCode== Activity.RESULT_OK){
            try{
                // Получение данных, введённых в окно "Пользователь"
                var name = data!!.getStringExtra("user_name")
                var age = data!!.getStringExtra("user_age")
                var email = data!!.getStringExtra("user_email")
                var languageid = data!!.getIntExtra("user_languageid",-1)
                // Добавление нового пользователя в БД
                //db.userDao().insertAll(User(null, name.toString(), age.toString().toInt(), email, languageid))
                soa_wrapper.CreateUser(REST_User(0, name.toString(), age.toString().toInt(), email.toString(),languageid))
            }catch (ex:Exception){
                tv_test.setText("Ошибка "+ex.message)
            }
        }

        // Обновление
        if(requestCode==2 && resultCode== Activity.RESULT_OK){
            try{
                // Получение данных, введённых в окно "Пользователь"
                var name = data!!.getStringExtra("user_name")
                var age = data!!.getStringExtra("user_age")
                var email = data!!.getStringExtra("user_email")
                var languageid = data!!.getIntExtra("user_languageid",-1)
                // Обновление нового пользователя в БД
                //db.userDao().update(User(edit_id, name.toString(), age.toString().toInt(), email, languageid))
                soa_wrapper.UpdateUser(edit_id,
                    REST_User(0,name.toString(), age.toString().toInt(), email.toString(),languageid))
            }catch (ex:Exception){
                tv_test.setText("Ошибка "+ex.message)
            }
        }
        // Удаление
        if(requestCode==2 && resultCode== Activity.RESULT_FIRST_USER){
            try{
                // Удаление из БД пользователя с id = edit_id
                //db.userDao().delete(User(edit_id,"",0,"", -1))
                soa_wrapper.DeleteUser(edit_id)
            }catch (ex:Exception){
                tv_test.setText("Ошибка "+ex.message)
            }
        }
        // Обновление списка в окне "Пользователи"
        RecyclerViewReloadData()
    }

    // Обработки нажатия на кнопку "Стрелка обратно"
    override fun onSupportNavigateUp(): Boolean {
        // По нажатии на кнопку "Стрелка обратно"
        // вернуться в предыдущее окно
        onBackPressed()
        return true
    }

    fun RecyclerViewReloadData(){
        // Обновить данные в RecyclerView
        try{
            // Установка соединения с базой данных
//            val db = Room.databaseBuilder(
//                applicationContext,
//                AppDatabase::class.java,
//                "my_database"
//            ).allowMainThreadQueries().build()
            soa_wrapper = RestWrapper()
            soa_wrapper.Init()
            //Очистка списка элементов
            rv_users_items.clear()
            // Загрузка в список данных из БД
            //rv_users_items.addAll(db.userDao().getAll())
            var users: MutableList<REST_User> = ArrayList()
            soa_wrapper.ListUsers()?.forEach {
                //users.add(REST_User(it.ID,it.Name,it.Age,it.Mail,it.LanguageID))
                rv_users_items.add(REST_User(it.ID,it.Name,it.Age,it.Mail,it.LanguageID))
            }
            // Уведомление RecyclerView о необходимости перерисовки
            adapter.notifyDataSetChanged()
        }catch (ex: Exception){
            tv_test.setText("Ошибка "+ex.message)
        }

    }

}