package com.example.pockethelperv1

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.preference.PreferenceManager
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.ActionBar
import kotlinx.android.synthetic.main.activity_questionnaire.*
import kotlinx.android.synthetic.main.activity_regestration.*


@Suppress("DEPRECATION")
class questionnaireActivity : AppCompatActivity()
{

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_questionnaire)

        val actionBar: ActionBar? = supportActionBar
        actionBar!!.setHomeButtonEnabled(true)
        actionBar.setDisplayHomeAsUpEnabled(true)

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
        val anketa = helpRequest(
            PreferenceManager.getDefaultSharedPreferences(baseContext).getString("profile_name", "не указано").toString(),
            PreferenceManager.getDefaultSharedPreferences(baseContext).getString("profile_numberPhone", "не указано").toString(),
            PreferenceManager.getDefaultSharedPreferences(baseContext).getString("profile_address", "не указано").toString())

        if (anketa.name == "" || anketa.numberPhone == "" || anketa.address == "")
        {
            toast(baseContext)
            return
        }



    }


    fun toast(applicationContext : Context)
    {
        val toast = Toast.makeText(applicationContext, "пожалуйста закончите регистрацию!", Toast.LENGTH_SHORT).show()
    }

}
