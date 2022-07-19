package com.example.myserviceclient

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import androidx.room.Room
import com.example.myserviceclient.mydata.AppDatabase
//import com.example.myserviceclient.mydata.entity.Language
import com.example.myserviceclient.soa.rest.REST_Language
import com.example.myserviceclient.soa.rest.RestWrapper

//import org.intellij.lang.annotations.Language

class ActivityUser : AppCompatActivity() {
    private lateinit var ed_name: EditText
    private lateinit var ed_age: EditText
    private lateinit var ed_email: EditText
    private lateinit var btn_go: Button
    private lateinit var btn_delete: Button
    private lateinit var spn_language: Spinner

    private lateinit var soa_wrapper : RestWrapper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user)

        // Задать заголовок окна
        supportActionBar!!.title = ("Пользователь")

        // Добавить кнопку "Стрелка обратно"
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        ed_name=findViewById(R.id.ed_name)
        ed_age=findViewById(R.id.ed_age)
        ed_email=findViewById(R.id.ed_email)
        spn_language = findViewById(R.id.spn_language)
        btn_go=findViewById(R.id.btn_go)
        btn_delete=findViewById(R.id.btn_delete)

        // Если режим редактирования
        if(intent.getIntExtra("is_edit",-1)==1){
            // Заполняем текстовые поля на основе данных,
            // принятых из родительского окна
            ed_name.setText(intent.getStringExtra("user_name"))
            ed_age.setText(intent.getStringExtra("user_age"))
            ed_email.setText(intent.getStringExtra("user_email"))
            // Заполняем выпадающий список языками программирования
            // программно выбираем из этого списка язык, принятый из
            // родительского окна
            Init_spn_language(intent.getIntExtra("user_languageid",-1))
            //
        } else{
            // Скрываем кнопку "Удалить", если Режим создания
            btn_delete.visibility = View.INVISIBLE
            // Заполняем выпадающий список языками программирования
            // программно выбираем из этого списка самый первый пункт
            // (строку "--Выберите язык программирования--")
            Init_spn_language(-1)
        }
        btn_go.setOnClickListener {
            // Формирование ответа - возврат данных обратно в первое окно
            val resultIntent = Intent()

            resultIntent.putExtra("user_name", ed_name.getText().toString())
            resultIntent.putExtra("user_age", ed_age.getText().toString())
            resultIntent.putExtra("user_email", ed_email.getText().toString())////////////////////////////////////////////////
            resultIntent.putExtra("user_languageid",
                (spn_language.adapter.getItem(spn_language.selectedItemPosition) as REST_Language).ID)
            // RESULT_OK в случае, ксли нажата кнопка "Сохранить"
            setResult(RESULT_OK, resultIntent)
            finish()
        }
        btn_delete.setOnClickListener {
            // Создание всплывающего окна для подтверждения удаления
            val builder = AlertDialog.Builder(this)
            // Задание текста предепреждения
            builder.setMessage("Вы действительно хотите удалить данного пользователя?")
            // Создание подтверждающей кнопки и задание действий, выполняемых
            // при нажатии на неё
            builder.setPositiveButton("Да"){ _,_->
                // Формирование ответа - возврат данных обратно в первое окно
                val resultIntent = Intent()
                // RESULT_FIRST_USER в случае, ксли нажата кнопка "Удалить"
                setResult(Activity.RESULT_FIRST_USER, resultIntent)
                finish()
            }
            // Создание отрицающей кнопки, при нажатии на
            // которойне будет выполнено никах действий
            builder.setNegativeButton("Нет"){_,_->}
            // Отображение созданного всплывающего окна
            builder.show()
        }
    }
    // Обработки нажатия на кнопку "Стрелка обратно"
    override fun onSupportNavigateUp(): Boolean {
        // По нажатии на кнопку "Стрелка обратно"
        // вернуться в предыдущее окно
        onBackPressed()
        return true
    }

    public fun Init_spn_language(active_language_id: Int =-1){
        var db: AppDatabase
        try{
            //Соединение с базой данных
//            db = Room.databaseBuilder(
//                applicationContext,
//                AppDatabase::class.java,
//                "my_database"
//            ).allowMainThreadQueries().build()

            soa_wrapper = RestWrapper()
            soa_wrapper.Init2()

            // Создание набора элементов, отображаемых в выпадающем списке
            var languages = ArrayList<REST_Language>()
            // Первый пункт - приглашение выбрать язык программирования
            languages.add(REST_Language(-1,"--Выберите язык программирования--"))
            // Остальные пункты из базы данных
            //languages.addAll(db.languageDao().getAll())
            //soa_wrapper.ListLanguages()

            var languages2: MutableList<REST_Language> = ArrayList()
            soa_wrapper.ListLanguages()?.forEach {
                languages2.add(REST_Language(it.ID,it.Name))
            }
            languages.addAll(languages2)

            // На На основе созданного набора элементов создаётся так называемый
            // адаптер данных - вспомогательный объект, определяющий способ отображения
            // этих элементов в выпадающем списке
            val dataAdapter = object : ArrayAdapter<REST_Language>(this,
                android.R.layout.simple_spinner_dropdown_item,languages){
                override fun isEnabled(position: Int): Boolean {
                    // Делаем первый пункт (строку "Выберите язык программирования")
                    return position !=0
                }
            }
            // Связывание созданного адптера данных с выпадающим списком
            spn_language.setAdapter(dataAdapter)

            // Выбор элемента, активного по умолчанию, если его
            // id был задан при вызове этой функции
            if(active_language_id>0)
                for(position in 0 until dataAdapter.getCount()){
                    if((spn_language.getItemAtPosition(position) as REST_Language).ID
                        == active_language_id){
                        spn_language.setSelection(position)
                        break
                    }
                }
        }catch (ex: Exception){
            // В случае ошибки выведем информацию на отладочную консоль
            Log.d("activity_user",ex.toString())
        }
    }


//End
}
