package com.example.myserviceclient

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class MainActivity : AppCompatActivity() {
    private lateinit var btn_users: Button
    private lateinit var btn_languages: Button
    private lateinit var btn_sync: Button
    private lateinit var btn_settings: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Задать заголовок окна
        supportActionBar!!.title = "Клиент веб сервиса mysite.ru"


        btn_users = findViewById(R.id.btn_ShowActivityUsers)

        btn_users.setOnClickListener{
            // Открытие окна "Пользователи"
            val my_intent = Intent(this,ActivityUsers::class.java)
            startActivity(my_intent)
        }

        btn_languages=findViewById(R.id.btn_ShowActivityLanguages)

        btn_languages.setOnClickListener{
            // Открытие окна языки программирования
            val my_intent = Intent(this,ActivityLanguages::class.java)
            startActivity(my_intent)
        }

        // Самостоятельно
        btn_sync=findViewById(R.id.btn_ShowActivitySync)

        btn_sync.setOnClickListener{
            // Открытие окна "Синхронизация с сервером"
            val my_intent = Intent(this,ActivitySync::class.java)
            startActivity(my_intent)
        }

        btn_settings=findViewById(R.id.btn_ShowActivitySettings)

        btn_settings.setOnClickListener{
            // Открытие окна "Настройки"
            val my_intent = Intent(this,ActivitySettings::class.java)
            startActivity(my_intent)
        }

    // End
    }
}