package com.example.pockethelperv1

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment

class CustomDialogFragment : DialogFragment()
{
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog
    {
        val text = arguments!!.getString("text_anket_Dialog")
        val builder = AlertDialog.Builder(activity)
        return builder
            .setTitle("Проверьте все данные")
            .setIcon(android.R.drawable.ic_dialog_alert)
            .setMessage(text)
            .setPositiveButton("Всё верно, завершить")
            {
                    dialog, which ->
                (activity as questionnaireActivity).sendAnketa()
            // вызов процедуры (метода кдасса <questionnaireActivity>), которая собираетобъект <Анкета> и отправляет на сервер (базу данных)
            }
            .setNegativeButton("Редактировать", null)
            .create();
    }

    /**
     *
     *
     * test
     * test
     * test
     *
     *
     */


}