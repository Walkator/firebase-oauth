package com.waniapps.firebaseoauth

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.text.InputType
import android.text.TextUtils
import android.transition.Slide
import android.transition.TransitionManager
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore


class RegisterActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var fStore: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {

        // Change color status bar
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        // Load components
        val edtFirstName: EditText = findViewById(R.id.edtFirstName)
        val edtLastName: EditText = findViewById(R.id.edtLastName)
        val edtEmail: EditText = findViewById(R.id.edtEmail)
        val edtPassword: EditText = findViewById(R.id.edtPassword)
        val edtRepeatPassword: EditText = findViewById(R.id.edtRepeatPassword)

        val btnSignInEmail: Button = findViewById(R.id.btnLoginEmail)
        val btnSignInBack: TextView = findViewById(R.id.btnSignInBack)
        var checkTerms: CheckBox = findViewById(R.id.checkTerms)

        // First capital letter
        edtFirstName.setInputType(InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_FLAG_CAP_SENTENCES)
        edtLastName.setInputType(InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_FLAG_CAP_SENTENCES)


        // Initialize Firebase Auth
        auth = FirebaseAuth.getInstance()
        fStore = FirebaseFirestore.getInstance()

        checkTerms.setOnClickListener{
            if(checkTerms.isChecked) {
                var dialog = PopUpTerms()
                dialog.show(supportFragmentManager, "customDialog")
            }
        }


        btnSignInEmail.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                if (TextUtils.isEmpty(edtFirstName.text.toString())) {
                    edtFirstName.setError("First name is required!")
                    return
                }

                if (TextUtils.isEmpty(edtLastName.text.toString())) {
                    edtLastName.setError("Last name is required!")
                    return
                }

                if (TextUtils.isEmpty(edtEmail.text.toString()) || !android.util.Patterns.EMAIL_ADDRESS.matcher(edtEmail.text.toString()).matches()) {
                    edtEmail.setError("Email is empty or invalid!")
                    return
                }

                if (edtPassword.text.length < 6) {
                    edtPassword.setError("Password mush be greater than 6 characters!")
                    return
                }

                if (edtRepeatPassword.text.toString() != edtPassword.text.toString()) {
                    edtRepeatPassword.setError("Password does not match!")
                    return
                }

                if(checkTerms.isChecked) {
                    createUser(
                        edtFirstName.text.toString(),
                        edtLastName.text.toString(),
                        edtEmail.text.toString(),
                        edtPassword.text.toString()
                    )
                } else {
                    Toast.makeText(applicationContext, "Check terms and conditions for use application!", Toast.LENGTH_SHORT).show()
                }
            }
        })

        btnSignInBack.setOnClickListener {
            finish()
        }
    }

    public override fun onStart() {
        super.onStart()
        val currentUser: FirebaseUser? = auth.currentUser
        if (currentUser != null) {
            val intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)
        }
    }

    private fun createUser(firstName: String, lastName: String, email: String, password: String) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information

                    // Save the user data in database
                    val user = hashMapOf(
                        "firstName" to firstName,
                        "lastName" to lastName,
                        "email" to email,
                    )

                    fStore.collection("users").document(user.getValue("email")).set(user)
                        .addOnSuccessListener {
                            Log.d("FireStore", "DocumentSnapshot added with ID: ${user.getValue("email")}")
                        }
                        .addOnFailureListener { e ->
                            Log.w("FireStore", "Error adding document", e)
                        }

                    onStart()
                } else {
                    // If sign in fails, display a message to the user.
                    Toast.makeText(
                        applicationContext,
                        "Error! Try another time.",
                        Toast.LENGTH_SHORT
                    ).show()
                }

                //hideProgressBar()
            }
    }
}