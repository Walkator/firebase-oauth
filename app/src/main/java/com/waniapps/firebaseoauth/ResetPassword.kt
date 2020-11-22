package com.waniapps.firebaseoauth

import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.google.firebase.auth.FirebaseAuth
import java.lang.Thread.sleep


class ResetPassword: AppCompatActivity()  {

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {

        // Change color status bar
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_resetpassword)

        var edtEmail: EditText = findViewById(R.id.edtEmail)
        var btnBack: ImageButton = findViewById(R.id.btnBack)
        var btnResetPassword: Button = findViewById(R.id.btnResetPassword)

        // Initialize Firebase Auth
        auth = FirebaseAuth.getInstance()

        btnBack.setOnClickListener() {
            finish()
        }

        btnResetPassword.setOnClickListener {
            if (TextUtils.isEmpty(edtEmail.text.toString()) || !android.util.Patterns.EMAIL_ADDRESS.matcher(
                    edtEmail.text.toString()
                ).matches()) {
                edtEmail.setError("Email is empty or invalid!")
            } else {
                auth.sendPasswordResetEmail(edtEmail.text.toString())
                    .addOnCompleteListener { task ->
                        if(task.isSuccessful) {
                            showAlert("Sent email.")
                        } else {
                            showAlert("Email do not exist!")
                        }
                    }
            }
        }
    }

    fun showAlert(textAlert: String) {
        var dialog = PopupAlerts()
        dialog.setText(textAlert)
        dialog.show(supportFragmentManager, "customDialog")
    }
}