package com.example.pockethelperv1

import android.content.Intent
import android.os.Bundle
import android.preference.PreferenceManager
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.marginEnd
import kotlinx.android.synthetic.main.activity_regestration.*

@Suppress("DEPRECATION")
class regestrationActivity : AppCompatActivity()
{
//    public data class Anketa(var UID: Int = 0)

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_regestration)

        val actionBar: ActionBar? = supportActionBar
        actionBar!!.setHomeButtonEnabled(true)
        actionBar.setDisplayHomeAsUpEnabled(true)

        proverkaAkkaunta = PreferenceManager.getDefaultSharedPreferences(baseContext).getBoolean("teastakk", false)
        if (proverkaAkkaunta)
        {
            regestrationButton.text = "изменить"
            loadProfile()
        }
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

    var proverkaAkkaunta : Boolean = false

    fun regestrationNewProfile(v : View)
    {
        if (name.text.isNotEmpty() && numberPhone.text.length == 11 && address.text.isNotEmpty())
        {
            saveProfile()
            proverkaAkkaunta = true
            save_proverkaAkkaunta()
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent) // запуск activity main
        }
    }

    fun saveProfile()
    {
        val editor = PreferenceManager.getDefaultSharedPreferences(baseContext).edit()
        editor.putString("profile_name", name.text.toString())
        editor.putString("profile_numberPhone", numberPhone.text.toString())
        editor.putString("profile_address", address.text.toString())
        editor.putBoolean("profile_valonter", valonter.isChecked)
        editor.apply()
    }

    fun loadProfile()
    {
        name.setText(PreferenceManager.getDefaultSharedPreferences(baseContext)
            .getString("profile_name", "не указано"))

        numberPhone.setText(PreferenceManager.getDefaultSharedPreferences(baseContext)
            .getString("profile_numberPhone", "не указано"))

        address.setText(PreferenceManager.getDefaultSharedPreferences(baseContext)
            .getString("profile_address", "не указано"))

        valonter.isChecked = PreferenceManager.getDefaultSharedPreferences(baseContext)
            .getBoolean("profile_valonter", false)
    }

    fun save_proverkaAkkaunta()
    {
        val editor = PreferenceManager.getDefaultSharedPreferences(baseContext).edit()
        editor.putBoolean("teastakk", proverkaAkkaunta)
        editor.apply()
    }

}
