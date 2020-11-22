package com.waniapps.firebaseoauth


import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider


class AuthMethodsActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private var mGoogleSignInClient: GoogleSignInClient? = null
    private val RC_SIGN_IN = 9001

    override fun onCreate(savedInstanceState: Bundle?) {

        // Splash Screen
        setTheme(R.style.AppTheme)

        // Change color status bar
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_auth_methods)

        // Load componentes
        val btnSignInEmail: Button = findViewById(R.id.btnLoginEmail)
        val btnSignInGoogle: Button = findViewById(R.id.btnLoginGoogle)
        val btnSignUp: TextView = findViewById(R.id.btnSignUp)
        val btnResetPassword: TextView = findViewById(R.id.btnForgotPassword)
        val edtEmail: EditText = findViewById(R.id.edtEmail)
        val edtPassword: EditText = findViewById(R.id.edtPassword)

        // Initialize Firebase Auth
        auth = FirebaseAuth.getInstance()

        // Load Google sign in
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);


        // Actions buttons
        // Button - Register
        btnSignUp.setOnClickListener() {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }

        //Button - Reset password
        btnResetPassword.setOnClickListener() {
            val intent = Intent(this, ResetPassword::class.java)
            startActivity(intent)
        }

        // Button - Sign In Email
        btnSignInEmail.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {

                if(TextUtils.isEmpty(edtEmail.text.toString()) || !android.util.Patterns.EMAIL_ADDRESS.matcher(edtEmail.text.toString()).matches()) {
                    edtEmail.setError("Email is empty or invalid!")
                    return
                }

                if(TextUtils.isEmpty(edtPassword.text.toString())) {
                    edtPassword.setError("Password mush be greater than 6 characters!")
                    return
                }

                checkUser(edtEmail.text.toString(), edtPassword.text.toString())
            }
        })

        // Button - Sign In Google Account
        btnSignInGoogle.setOnClickListener {
            signIn()
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

    private fun signIn() {
        val signInIntent = mGoogleSignInClient?.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                // Google Sign In was successful, authenticate with Firebase
                val account = task.getResult(ApiException::class.java)!!
                firebaseAuthWithGoogle(account.idToken!!)
            } catch (e: ApiException) {
                // Google Sign In failed, update UI appropriately
                Toast.makeText(applicationContext, "Error! Try another time.",Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun firebaseAuthWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    onStart()
                } else {
                    // If sign in fails, display a message to the user.
                    Toast.makeText(applicationContext, "Error! Try another time.",Toast.LENGTH_SHORT).show()
                }
            }
    }


    private fun checkUser(email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    onStart()
                } else {
                    // If sign in fails, display a message to the user.
                    Toast.makeText(applicationContext, "Incorrect password.",Toast.LENGTH_SHORT).show()
                }

                //hideProgressBar()
            }
    }
}