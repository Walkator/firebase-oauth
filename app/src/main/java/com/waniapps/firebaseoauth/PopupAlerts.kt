package com.waniapps.firebaseoauth

import android.R
import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.view.View
import androidx.fragment.app.DialogFragment
import kotlinx.android.synthetic.main.popup_alert.view.*


class PopupAlerts: DialogFragment() {

    private var textAlert: String = ""

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder: AlertDialog.Builder = AlertDialog.Builder(activity)
        val inflater = activity!!.layoutInflater
        val v: View = inflater.inflate(com.waniapps.firebaseoauth.R.layout.popup_alert, null)
        builder.setView(v)

        v.txtAlert.text = textAlert
        v.btnPopup.setOnClickListener() {
            dismiss()
        }

        return builder.create();
    }

    fun setText(text: String) {
        this.textAlert = text
    }
}