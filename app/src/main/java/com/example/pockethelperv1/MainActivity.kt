package com.example.pockethelperv1

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.preference.PreferenceManager
import android.view.View





class MainActivity : AppCompatActivity()
{
    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        var proverkaAkkaunta = PreferenceManager.getDefaultSharedPreferences(baseContext).getBoolean("teastakk", false)
        if (!proverkaAkkaunta) InRegestration()

    }


    fun sendMessage(v : View)
    {
        // действия, совершаемые после нажатия на кнопку
        // Создаем объект Intent для вызова новой Activity
        val intent = Intent(this, questionnaireActivity::class.java)
        // запуск activity
        startActivity(intent)
    }

    fun InRegestration()
    {
        val intent = Intent(this, regestrationActivity::class.java)
        // передача объекта с ключом "regestrationStatus" и значением proverkaAkkaunta
        //intent.putExtra("regestrationStatus", proverkaAkkaunta)
        startActivity(intent)
    }

    fun settingProfile(v : View)
    {
        val intent = Intent(this, regestrationActivity::class.java)
        startActivity(intent)   // запуск activity regestration
    }

/*    fun save(v: View)
    {
        val editor = PreferenceManager.getDefaultSharedPreferences(baseContext).edit()
        editor.putString("aaa", editText.text.toString())
        editor.commit()
    }*/



}
