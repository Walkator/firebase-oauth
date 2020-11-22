package com.waniapps.firebaseoauth

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.EventListener
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException


class HomeActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var fStore: FirebaseFirestore
    private lateinit var txtWelcome: TextView

    override fun onCreate(savedInstanceState: Bundle?) {

        // Change color status bar
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        // Initialize Firebase Auth
        auth = FirebaseAuth.getInstance()
        fStore = FirebaseFirestore.getInstance()

        txtWelcome = findViewById(R.id.txtWelcome)
        var btnLogOut: Button = findViewById(R.id.btnLogOut)

        getNameDB()

        btnLogOut.setOnClickListener {
            signOut()
            backAuthActivity()
        }
    }

    private fun signOut() {
        auth.signOut()
    }

    private fun backAuthActivity() {
        val intent = Intent(this, AuthMethodsActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
        startActivity(intent)
    }

    private fun getNameDB() {
        var name: String = ""
        var query = fStore.collection("users").document(auth.currentUser?.email.toString())
        query.addSnapshotListener(object : EventListener<DocumentSnapshot?> {
            override fun onEvent(value: DocumentSnapshot?, error: FirebaseFirestoreException?) {
                if (value != null && value.exists()) {
                    name = value?.data?.getValue("firstName").toString() + " " + value?.data?.getValue("lastName").toString()
                    txtWelcome.text = "Welcome " + name + "!"
                } else {
                    var str: String = auth.currentUser?.email.toString()
                    if(android.util.Patterns.EMAIL_ADDRESS.matcher(str).matches()) {
                        name = str.substring(0, str.indexOf('@'))
                        txtWelcome.text = "Welcome " + name + "!"
                    }
                }
            }
        })
    }
}