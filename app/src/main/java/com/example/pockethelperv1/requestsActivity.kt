package com.example.pockethelperv1

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.Button
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.ActionBar
import kotlinx.android.synthetic.main.activity_requests.*


class requestsActivity : AppCompatActivity()
{
    var anketsList: MutableCollection<Anketa> = mutableListOf()
    var countID = 0
    var myhelp = myHelper()

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_requests)

        val actionBar: ActionBar? = supportActionBar
        actionBar!!.setHomeButtonEnabled(true)
        actionBar.setDisplayHomeAsUpEnabled(true)

        anketsList = myhelp.loadAnkets() //инициализация списка анкет

//        if (PreferenceManager.getDefaultSharedPreferences(baseContext).getBoolean("profile_valonter", false))
//        {

//        }
//        else
//        {
//
//        }

        anketsList.forEach{
            val btn = Button(applicationContext)
            btn.text = it.name + "\n" + it.phone + "\n" + it.address + "\n" + "Тип помощи: " + it.help_View
            btn.layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT)
            btn.id = countID
            btn.setOnClickListener{ v -> requestLayout!!.removeView(v) }
            requestLayout!!.addView(btn)
            countID++
        }
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
