package com.farid.riaurental

import android.app.ProgressDialog
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import com.farid.riaurental.Model.BookingModel
import com.farid.riaurental.Model.RentModel
import com.farid.riaurental.Model.UserModel
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_booking_rent.*
import java.text.DecimalFormat
import java.util.*

class BookingRentActivity : AppCompatActivity() {

    private val db = Firebase.firestore
    private lateinit var rent : RentModel
    private lateinit var tenant : UserModel

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_booking_rent)

        val rentId = intent.getStringExtra("rentId")
        val user = Firebase.auth.currentUser

        user?.let {
            for (profile in it.providerData) {
                val email = profile.email
//                Log.d("PROFILE", email)
                db.collection("users").whereEqualTo("email", email).limit(1)
                    .addSnapshotListener { snapshot, _ ->
                        if (snapshot != null) {
                            for (data in snapshot) {
                                tenant = data.toObject(UserModel::class.java)!!
                            }
                        }
                    }
            }
        }

        rentId?.let {
            db.collection("rents").document(rentId)
                .addSnapshotListener { snapshot, _ ->
                    if(snapshot != null){
                        rent = snapshot.toObject(RentModel::class.java)!!

                        textName.text = rent.name
                        textAddress.text = rent.address

                        val duration : String
                        if (rent.duration == "d") {
                            duration = "/Day"
                        }
                        else if(rent.duration == "w") {
                            duration = "/Week"
                        }
                        else if(rent.duration == "m") {
                            duration = "/Month"
                        }
                        else {
                            duration = "/Year"
                        }
                        textDuration.text = duration

                        val dec = DecimalFormat("#,###")
                        textPrice.text = "Rp. "+ dec.format(rent.price).toString()

                        if (rent.pictures.isEmpty()) {
                            imageView.setImageResource(R.drawable.house_placeholder)
                        }
                        else{
                            Picasso.get().load(rent.pictures.first().toString()).into(imageView)
                        }

                        btnBooking.setOnClickListener {
                            val rentId = rent.id
                            val tenantId = tenant.id
                            val ownerId = rent.ownerId
                            val rentDate = inputDate.text.toString()
                            val rentDuration = inputDuration.text.toString()
                            val price = inputPrice.text.toString()
                            val source = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890"
                            val rentCode = getRandomString(7)
                            val createdAt = Calendar.getInstance().toString()

                            if (rentDate.isNullOrEmpty() || rentDuration.isNullOrEmpty()  || price.isNullOrEmpty() ) {
                                Toast.makeText(this, "Please fill out all fields", Toast.LENGTH_SHORT).show()
                                return@setOnClickListener
                            }

                            val progressDialog = ProgressDialog(this, R.style.Theme_MaterialComponents_Light_Dialog)
                            progressDialog.isIndeterminate = true
                            progressDialog.setMessage("Loading...")
                            progressDialog.show()

                            bookRent(rentId, tenantId, ownerId, rentDate, rentDuration.toInt(), price.toInt(), rentCode, createdAt)
                            progressDialog.hide()
                        }
                    }
                }
        }
    }

    fun getRandomString(length: Int) : String {
        val allowedChars = ('A'..'Z') + ('0'..'9')
        return (1..length)
            .map { allowedChars.random() }
            .joinToString("")
    }

    private fun bookRent(rentId: String, tenantId: String, ownerId: String, rentDate: String, rentDuration: Int, price: Int, rentCode: String, createdAt: String) {
        val ref = db.collection("booking").document()

        val booking = BookingModel(rentId, tenantId, ownerId, rentDate, rentDuration, price, rentCode, createdAt, ref.id)

        db.collection("bookings").document(ref.id).set(booking)
            .addOnSuccessListener { result ->
                Toast.makeText(this, "Booking Success",Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Booking Failed",Toast.LENGTH_SHORT).show()
            }
    }
}