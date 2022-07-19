package com.example.myserviceclient

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import androidx.room.Room
import java.lang.Exception
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

import com.example.myserviceclient.mydata.AppDatabase
//import com.example.myserviceclient.mydata.User
//import com.example.myserviceclient.mydata.entity.Language
import com.example.myserviceclient.soa.rest.REST_Language
import com.example.myserviceclient.soa.rest.RestWrapper
import com.google.android.material.floatingactionbutton.FloatingActionButton


class ActivityLanguages : AppCompatActivity() {
    private lateinit var tv_test: TextView
    private lateinit var fab_add:FloatingActionButton
    private lateinit var rv_languages: RecyclerView
    private lateinit var adapter: RecyclerViewAdapter_Languages
    private lateinit var rv_languages_items: ArrayList<REST_Language>
    //private lateinit var db:AppDatabase
    private lateinit var my_intent: Intent

    private lateinit var soa_wrapper : RestWrapper

    private var edit_id: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_languages)

        // Задать заголовок окна
        supportActionBar!!.title = ("Языки программирования")

        // Добавить кнопку "Стрелка обратно"
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        // Создание объекта для доступа к окну "Пользователь"
        my_intent = Intent(this,ActivityLanguage::class.java)

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
            soa_wrapper.Init2()
        }catch (ex: Exception){
            tv_test.setText("Ошибка "+ex.message)
        }


        fab_add = findViewById(R.id.fab_add)

        fab_add.setOnClickListener{
            val my_intent = Intent(this, ActivityLanguage::class.java)
            // putExtra - передача в открываемое (второе) окно некоторых данных
            my_intent.putExtra("is_edit",0)

            // Откроем окно для создания нового
            startActivityForResult(my_intent,1)
        }

        // Первоначальная настройка RecyclerView
        // получение доступа  \к элементу RecyclerView, расположенному на форме
        rv_languages=findViewById(R.id.rv_languages)

        // Задание в качестве компоновочного контейнера для элементов списка
        // компонента LinearLayoutManager с вертикальным расположением элементов
        val linearLayoutManager = LinearLayoutManager(applicationContext)
        linearLayoutManager.orientation = LinearLayoutManager.VERTICAL
        rv_languages.layoutManager = linearLayoutManager

        // Создание массива элементов, через который в RecyclerView будут поступать
        // элементы списка
        rv_languages_items = ArrayList<REST_Language>()
        // Создание экземпляра объекта RecyclerViewAdapter_Users,
        // определяющего логику работы элемента RecyclerView
        adapter = RecyclerViewAdapter_Languages(rv_languages_items, object:RecyclerViewOnClickListenter {
            // Обработчик шелчка на элементе списка
            override fun onClicked(idx: Int) {
                //// Выводим имя выбранного пользователя в заголовке окна
                //supportActionBar!!.title="Вы выбрали: " + rv_users_items[idx-1].Name
                try{
                    // ОТкрытие окна "Пользователь" для редактирования данных
                    // Определяем id пользователя выбранного в списке
                    edit_id=idx
                    // Извлечения данных выбранного пользователя из БД по его id
                    //val language = db.languageDao().getLanguageById(edit_id)
                    val REST_Language = soa_wrapper.ReadLanguage(idx)


                    // Передаём открываемому окну флаг is_edit=1
                    // чтобы окно открылось в режиме "Редактирование"
                    my_intent.putExtra("is_edit",1)

                    // Передача в открываемое окно данных редактируемого пользователя
                    //my_intent.putExtra("language_name", language.Name);
                    val language = soa_wrapper.ReadLanguage(idx)
                    //my_intent.putExtra("language_name",language?.Name);
                    my_intent.putExtra("language_name",REST_Language?.Name);


                    // Открытие окна
                    startActivityForResult(my_intent,2)
                }catch (ex:Exception){
                    // Если при открытие окна произошла ошибка - выведем её
                    tv_test.setText("Ошибка "+ex.message)
                }

            }
        })

        rv_languages.adapter = adapter

        // Тестируем работу RecyclerView
        RecyclerViewReloadData()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        // Обработчик завершения дочернего окна

        // Создание
        if(requestCode==1 && resultCode== Activity.RESULT_OK){
            try{
                // Получение данных, введённых в окно "Пользователь"
                var name = data!!.getStringExtra("language_name")
                // Добавление нового пользователя в БД
                //db.languageDao().insertAll(Language(null, name.toString()))
                soa_wrapper.CreateLanguage(REST_Language(0, name.toString()))
            }catch (ex:Exception){
                tv_test.setText("Ошибка "+ex.message)
            }
        }

        // Обновление
        if(requestCode==2 && resultCode== Activity.RESULT_OK){
            try{
                // Получение данных, введённых в окно "Пользователь"
                var name = data!!.getStringExtra("language_name")

                // Добавление нового пользователя в БД
                //db.languageDao().update(Language(edit_id, name.toString()))
                soa_wrapper.UpdateLanguage(edit_id, REST_Language(0,name.toString()))
            }catch (ex:Exception){
                tv_test.setText("Ошибка "+ex.message)
            }
        }
        // Удаление
        if(requestCode==2 && resultCode== Activity.RESULT_FIRST_USER){
            try{
                // Удаление из БД пользователя с id = edit_id
                //db.languageDao().delete(Language(edit_id,""))
                soa_wrapper.DeleteLanguage(edit_id)
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
            soa_wrapper.Init2()
            //Очистка списка элементов
            rv_languages_items.clear()
            // Загрузка в список данных из БД
            //rv_languages_items.addAll(db.languageDao().getAll())
            //var languages: MutableList<Language> = ArrayList()
            soa_wrapper.ListLanguages()?.forEach {
                //languages.add(Language(it.uid,it.Name))
                rv_languages_items.add(REST_Language(it.ID,it.Name))// Дабавить в RecyclerView
            }
            // Уведомление RecyclerView о необходимости перерисовки
            adapter.notifyDataSetChanged()
        }catch (ex: Exception){
            tv_test.setText("Ошибка "+ex.message)
        }

    }

}