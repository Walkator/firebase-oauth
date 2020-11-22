package com.waniapps.firebaseoauth

import android.R
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import kotlinx.android.synthetic.main.popup_termsandconditions.view.*

class PopUpTerms: DialogFragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        var rootView: View = inflater.inflate(com.waniapps.firebaseoauth.R.layout.popup_termsandconditions, container, false)

        rootView.btnPopup.setOnClickListener() {
            dismiss()
        }

        return  rootView
    }
}