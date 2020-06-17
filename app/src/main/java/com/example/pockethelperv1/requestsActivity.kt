package com.example.pockethelperv1

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.preference.PreferenceManager
import android.view.MenuItem
import android.widget.Button
import android.widget.LinearLayout
import androidx.appcompat.app.ActionBar
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_requests.*

@Suppress("DEPRECATION")
class requestsActivity : AppCompatActivity()
{
    var database = FirebaseDatabase.getInstance()
    var myRef = database.reference
    var anketsList: MutableCollection<Anketa> = mutableListOf()
    var countID = 0

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_requests)

        val actionBar: ActionBar? = supportActionBar
        actionBar!!.setHomeButtonEnabled(true)
        actionBar.setDisplayHomeAsUpEnabled(true)

        //-----------------------------------------
        val menuListener = object : ValueEventListener
        {
            override fun onDataChange(dataSnapshot: DataSnapshot)
            {
                //Очищаем список сообщений
                anketsList.clear()
                // Тут происходит МАГИЯ!
                // Из потока данных, возвращаемых из БД, который есть текстовое представление списка объектов
                // восстанавливаются объекты типа Message и записываются в список messageList
                dataSnapshot.children.mapNotNullTo(anketsList) { it.getValue<Anketa>(Anketa::class.java) }
                requestLayout.removeAllViews()
                // Полезная работа - в данном случае, посторочно пишем сообщения в текстбокс
                if (PreferenceManager.getDefaultSharedPreferences(baseContext).getBoolean("profile_valonter", false))
                {
                    anketsList.forEach {
                        val btn = Button(applicationContext)
                        btn.text = it.name + "\n" + it.phone + "\n" + it.address + "\n" + "Тип помощи: " + it.help_View
                        btn.layoutParams = LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT)
                        btn.id = countID
                        btn.setOnClickListener{
                                v -> requestLayout!!.removeView(v)
                        }
                        requestLayout!!.addView(btn)
                        countID++

                    }
                } else
                {

                }

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
        return when (item.itemId)
        {
            android.R.id.home -> {
                finish()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }


}
