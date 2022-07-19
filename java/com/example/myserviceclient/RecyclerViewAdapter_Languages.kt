package com.example.myserviceclient

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import java.util.ArrayList

//import com.example.myserviceclient.mydata.entity.Language
import com.example.myserviceclient.soa.rest.*

class RecyclerViewAdapter_Languages (
    //private val rv_items: ArrayList<Language>,
    private val rv_items: ArrayList<REST_Language>,
    private val onClickListenter: RecyclerViewOnClickListenter
) : RecyclerView.Adapter<RecyclerViewAdapter_Languages.MyViewHolder>(){
    inner class MyViewHolder(view:View) : RecyclerView.ViewHolder(view){
        // Связывание текстовых полей, объявленных в файле разметки (recycler_item_language.xml)
        // с переменными в данном коде
        val tv_name: TextView = view.findViewById<View>(R.id.tv_name) as TextView
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.recycler_item_language, parent,false)
        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        // Эта функция вызывается последовательно для каждого элемента
        // списка RecyclerView. holder - экземпляр объекта разметки,
        // созданный для отображения текущего элементаю зщышешщт -
        // порядковый номер текущего элемента

        // Получение данных для текущего элемента списка из
        // переданного в конструктор массива
        val rw_item = rv_items[position]
        // Вывод свойств полученного объекта данных в соответствующие
        // текстовые поля разметки
        holder.tv_name.text = rw_item.Name

        // Назначение текущему элементу списка обработчика для события Click
        holder.itemView.setOnClickListener {
            onClickListenter.onClicked(rw_item.ID as Int)
        }
    }

    override fun getItemCount(): Int {
        // Получение количества элементов в списке
        return rv_items.size
    }

}