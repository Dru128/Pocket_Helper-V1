package com.example.pockethelperv1

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.preference.PreferenceManager
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.ActionBar
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_questionnaire.*
import kotlinx.android.synthetic.main.activity_regestration.*


@Suppress("DEPRECATION")
class questionnaireActivity : AppCompatActivity()
{
    var database = FirebaseDatabase.getInstance()
    var myRef = database.reference
    var anketsList: MutableCollection<Anketa> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_questionnaire)

        val actionBar: ActionBar? = supportActionBar
        actionBar!!.setHomeButtonEnabled(true)
        actionBar.setDisplayHomeAsUpEnabled(true)

        //-----------------------------------------
        val menuListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                //Очищаем список сообщений
                anketsList.clear()
                // Тут происходит МАГИЯ!
                // Из потока данных, возвращаемых из БД, который есть текстовое представление списка объектов
                // восстанавливаются объекты типа Message и записываются в список messageList
                dataSnapshot.children.mapNotNullTo(anketsList) { it.getValue<Anketa>(Anketa::class.java) }
//                myText.text = ""
                // Полезная работа - в данном случае, посторочно пишем сообщения в текстбокс
//                anketsList.forEach {
//                    myText.append("${it.Author}: ${it.Text}\n")
//                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                println("loadPost:onCancelled ${databaseError.toException()}")
            }
        }

        /**
         * Подписка на событие изменения списка - добавление листенера
         * Почитать про листенеры и поэкспериментировать с разными листенерами
         * они отличаются по событиям: изменение свойств существующего объекта, появление новых
         * дочерних объектов и т.д.
         */
        //myRef.child("messages").addListenerForSingleValueEvent(menuListener)
        myRef.child("messages").addValueEventListener(menuListener)
        //------------------------------------------

    }

    override fun onOptionsItemSelected(item: MenuItem) : Boolean
    {
        return when (item.getItemId())
        {
            android.R.id.home -> {
                finish()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    fun sendRequest(v : View)
    {
        if (PreferenceManager.getDefaultSharedPreferences(baseContext).getBoolean("profile_valonter", false))
        {
            Toast.makeText(applicationContext, "Волонтёр не может размещать заявки!", Toast.LENGTH_SHORT).show()
            return
        }

        val profile_name = PreferenceManager.getDefaultSharedPreferences(baseContext).getString("profile_name", "не указано").toString()
        val profile_numberPhone = PreferenceManager.getDefaultSharedPreferences(baseContext).getString("profile_numberPhone", "не указано").toString()
        val profile_address = PreferenceManager.getDefaultSharedPreferences(baseContext).getString("profile_address", "не указано").toString()

        if (profile_name == "" || profile_numberPhone == "" || profile_address == "")
        {
            Toast.makeText(applicationContext, "Пожалуйста закончите регистрацию!", Toast.LENGTH_SHORT).show()
            return
        }

        val numberHelp = 1//--------------------------------------
        val id : Int = getId()    //  id анкеты
//        val text : String = a + "\n" + b + "\n" + c + "\n" + numberHelp



        //----------------------------------------------------

        // Сохранение в БД
        // Создали объект, который будем писать в базу
        val msg : Anketa = Anketa(
            ID = id,
            help_View = 111,
            name = profile_name,
            phone = profile_numberPhone,
            address = profile_address)

        // Записали пустой дочерний узел и получили ключ
        val key = myRef.child("messages").push().key
        // Если узел создался, то находим его по ключу и присваиваем ему нащ объект
        if (key != null)
        {
            myRef.child("messages").child(key).setValue(msg)
        }
        Toast.makeText(applicationContext, "Заявка принята!", Toast.LENGTH_SHORT).show()
    }

    fun getId() : Int
    {
    var max_id : Int = 0
        anketsList.forEach{
            if (it.ID > max_id) max_id = it.ID
        }
        max_id++
    return max_id    //-----------------------------------------------
    }


}
