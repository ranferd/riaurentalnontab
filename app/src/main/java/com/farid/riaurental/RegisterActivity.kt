package com.farid.riaurental

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.farid.riaurental.Model.UserModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_register.*

class RegisterActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private val db = Firebase.firestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        auth = FirebaseAuth.getInstance()

        btnRegister.setOnClickListener {
            val name = inputName.text.toString()
            val email = inputEmail.text.toString()
            val password = inputPassword.text.toString()
            val passwordConfirm = inputPasswordConfirm.text.toString()
            val phone = inputPhone.text.toString()
            val address = inputAddress.text.toString()
            val genderId = inputGender.checkedRadioButtonId
            val gender = resources.getResourceEntryName(genderId)

            if (name.isNullOrEmpty() || email.isNullOrEmpty() || password.isNullOrEmpty() || passwordConfirm.isNullOrEmpty() || phone.isNullOrEmpty() || address.isNullOrEmpty() || gender.isNullOrEmpty()) {
                Toast.makeText(this, "Please fill out all fields", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if (password != passwordConfirm) {
                Toast.makeText(this, "Please check your password", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if (password.length < 8) {
                Toast.makeText(this, "Password must be at least 8 character", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val progressDialog = ProgressDialog(this, R.style.Theme_MaterialComponents_Light_Dialog)
            progressDialog.isIndeterminate = true
            progressDialog.setMessage("Loading...")
            progressDialog.show()

            auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
//                Toast.makeText( this, "createUserWithEmail:onComplete:" + task.isSuccessful, Toast.LENGTH_SHORT).show()

                // If sign in fails, display a message to the user. If sign in succeeds
                // the auth state listener will be notified and logic to handle the
                // signed in user can be handled in the listener.
                if (!task.isSuccessful) {
                    Toast.makeText(
                        this,
                        "Registration failed." + task.exception,
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    Toast.makeText(this, "Registration successful.", Toast.LENGTH_SHORT).show()
                    writeNewUser(email, name, phone, address, gender)
                    startActivity(Intent(this, MainActivity::class.java))
                    finish()
                }
            }
            progressDialog.hide()
        }
    }

    private fun writeNewUser(Email: String, Name: String, Phone: String, Address: String, Gender: String) {
        val ref = db.collection("users").document()

        val user = UserModel(Email, Name, Phone, Address, Gender, ref.id)
        db.collection("users").document(ref.id).set(user)
            .addOnSuccessListener { result ->
                Toast.makeText(this, "Register Success",Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Register Failed",Toast.LENGTH_SHORT).show()
            }
    }
}