package com.farid.riaurental

import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_forgot_password.*

class ForgotPasswordActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgot_password)

        auth = FirebaseAuth.getInstance()

        btnForgotPassword.setOnClickListener {
            val email = inputEmail.text.toString()
            if (email.isEmpty()) {
                Toast.makeText(this, "Please Insert Email", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            val progressDialog = ProgressDialog(this, R.style.Theme_MaterialComponents_Light_Dialog)
            progressDialog.isIndeterminate = true
            progressDialog.setMessage("Loading...")
            progressDialog.show()

            auth.sendPasswordResetEmail(email)
                .addOnCompleteListener {
                    if (!it.isSuccessful) {
                        Toast.makeText(this, "Failed Reset Password", Toast.LENGTH_SHORT).show()
                    }
                    else {
                        Toast.makeText(this, "Email sent", Toast.LENGTH_SHORT).show()
                        startActivity(Intent(this, MainActivity::class.java))
                    }
                }
                .addOnFailureListener{
                    Log.d("Main", "Failed Reset Password: ${it.message}")
                    Toast.makeText(this, "Email incorrect", Toast.LENGTH_SHORT).show()
                }
            progressDialog.hide()
        }
    }
}