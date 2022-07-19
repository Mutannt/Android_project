package com.example.myserviceclient

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class ActivitySettings : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        // Задать заголовок окна
        supportActionBar!!.title = ("Настройки")

        // Добавить кнопку "Стрелка обратно"
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
    }
    // Обработки нажатия на кнопку "Стрелка обратно"
    override fun onSupportNavigateUp(): Boolean {
        // По нажатии на кнопку "Стрелка обратно"
        // вернуться в предыдущее окно
        onBackPressed()
        return true
    }
//End
}