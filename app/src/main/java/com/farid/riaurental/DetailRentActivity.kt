package com.farid.riaurental

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.farid.riaurental.Model.RentModel
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_rent_detail.*
import java.text.DecimalFormat

class DetailRentActivity : AppCompatActivity() {

    private val db = Firebase.firestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_rent_detail)

        val rentId =  intent.getStringExtra("rentId")

        rentId?.let {
            db.collection("rents").document(rentId)
                .addSnapshotListener { snapshot, _ ->
                    if(snapshot != null){
                        val rent = snapshot.toObject(RentModel::class.java)

                        if (rent != null) {
                            rentName.text = rent.name
                            rentAddress.text = rent.address
                            rentDescription.text = rent.detail

                            if (rent.type == 1) {
                                rentType.text = rent.quantity.toString() + " Room left"
                            } else {
                                rentType.text = rent.quantity.toString() + " House left"
                            }

                            rentDuration.text = if (rent.duration == "d") {
                                "/Day"
                            } else if(rent.duration == "w") {
                                "/Week"
                            } else if(rent.duration == "m") {
                                "/Month"
                            } else {
                                "/Year"
                            }

                            rentPrice.text = "Rp. "+ DecimalFormat("#,###").format(rent.price).toString()

                            if (rent.pictures.isEmpty()) {
                                rentPicture.setImageResource(R.drawable.house_placeholder)
                            }
                            else{
                                Picasso.get().load(rent.pictures.first().toString()).into(rentPicture)
                            }

                            btnBook.setOnClickListener {
                                showBooking(rent)
                            }
                        }
                    }
                }
        }
    }

    private fun showBooking(rent: RentModel) {
        val intent = Intent(this, BookingRentActivity::class.java)
        intent.putExtra("rentId", rent.id)
        this.startActivity(intent)
    }
}