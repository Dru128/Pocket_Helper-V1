package com.example.pockethelperv1

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import android.os.Bundle
import android.preference.PreferenceManager
import android.text.format.DateUtils
import android.view.MenuItem
import android.view.View
import android.widget.RadioButton
import android.widget.Toast
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_questionnaire.*
import java.util.*


@Suppress("DEPRECATION")
class questionnaireActivity : AppCompatActivity() {
    var database = FirebaseDatabase.getInstance()
    var myRef = database.reference
    var anketsList: MutableCollection<Anketa> = mutableListOf()
    var myhelp = myHelper()
    var dateAndTime = Calendar.getInstance()
    lateinit var DATA : String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_questionnaire)

        val actionBar: ActionBar? = supportActionBar
        actionBar!!.setHomeButtonEnabled(true)
        actionBar.setDisplayHomeAsUpEnabled(true)

        setInitialDateTime()
        anketsList = myhelp.loadAnkets() //инициализация списка анкет



    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.getItemId()) {
            android.R.id.home -> {
                finish()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    fun sendRequest(v: View)
    {
        if (myhelp.load_profile_valonter(baseContext))
        {
            Toast.makeText(applicationContext,"Волонтёр не может размещать заявки!", Toast.LENGTH_SHORT).show()
            return
        }

        val help_index: Int = myGroup.indexOfChild(findViewById(myGroup.checkedRadioButtonId))
        if (help_index == -1)
        {
            Toast.makeText(applicationContext, "Необходимо выбрать тип помощи!", Toast.LENGTH_SHORT).show()
            return
        }

        if (myhelp.load_address(baseContext) == "" || myhelp.load_Name(baseContext) == "" || myhelp.load_Phone(baseContext) == "")
        {
            Toast.makeText(applicationContext,"Пожалуйста закончите регистрацию!", Toast.LENGTH_SHORT).show()
            return
        }

        DatePickerDialog(
            this@questionnaireActivity, dataListener,
            dateAndTime[Calendar.YEAR],
            dateAndTime[Calendar.MONTH],
            dateAndTime[Calendar.DAY_OF_MONTH]
        ).show()
        Toast.makeText(applicationContext,"Выберете Дату, когда к вам должен зайти валантёр!", Toast.LENGTH_LONG).show()
    }


    // установка начальных даты и времени
    private fun setInitialDateTime()
    {
        DATA = DateUtils.formatDateTime(
            this,
            dateAndTime.timeInMillis,
            DateUtils.FORMAT_SHOW_DATE or DateUtils.FORMAT_SHOW_YEAR
                    or DateUtils.FORMAT_SHOW_TIME
        )
    }

    // установка обработчика выбора времени
    var timeListener =
        TimePickerDialog.OnTimeSetListener { _, hourOfDay, minute ->
            dateAndTime[Calendar.HOUR_OF_DAY] = hourOfDay
            dateAndTime[Calendar.MINUTE] = minute
            setInitialDateTime()    //  Запись даты в текст

            //  Если до сюда дойдёт, то дата и время выбраны и нужно будет сохранить дату -----------------------------------------------------------------------------------------------
            sendAnketa() // вызов процедуры, которая собираетобъект <Анкета> и отправляет на сервер (базу данных)
        }

    // установка обработчика выбора даты
    var dataListener =
        DatePickerDialog.OnDateSetListener { _, year, monthOfYear, dayOfMonth ->
            dateAndTime[Calendar.YEAR] = year
            dateAndTime[Calendar.MONTH] = monthOfYear
            dateAndTime[Calendar.DAY_OF_MONTH] = dayOfMonth

//  Выбор времени, если в диалоговом окне "Дата" была нажата кнопка "OK"
            TimePickerDialog(
                this@questionnaireActivity, timeListener,
                dateAndTime[Calendar.HOUR_OF_DAY],
                dateAndTime[Calendar.MINUTE], true
            ).show()

            Toast.makeText(applicationContext,"Выберете время, в которое к вам должен зайти валантёр!", Toast.LENGTH_LONG).show()

        }

    fun sendAnketa()
    {
        val id: Int = myhelp.getID()    //  id анкеты
        val checkedRadioButtonId: Int = myGroup.checkedRadioButtonId
        val help_view = findViewById<View>(checkedRadioButtonId) as RadioButton

        val anket: Anketa = Anketa(
            ID = id,
            help_View = help_view.text.toString(),
            name = myhelp.load_Name(baseContext),
            phone = myhelp.load_Phone(baseContext),
            address = myhelp.load_address(baseContext),
            dataAndTime = DATA
        )

        myhelp.saveAnket(anket)

        Toast.makeText(applicationContext, "Заявка принята!", Toast.LENGTH_LONG).show()
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }
}