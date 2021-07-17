package com.farid.riaurental

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.farid.riaurental.Model.RentModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.rent_detail.*
import java.text.DecimalFormat

class DetailRentActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.rent_detail)


        val ref = FirebaseDatabase.getInstance().getReference("rents")
        val rentId =  intent.getStringExtra("rentId")
        rentId?.let {
            ref.child(it).addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if(snapshot.exists()){
                        Log.e("FIREBASE", snapshot.getValue(RentModel::class.java).toString())
                        val result = snapshot.getValue(RentModel::class.java)

                        if (result != null) {
                            rentName.text = result.name
                            rentAddress.text = result.address

                            val duration : String
                            if (result.duration == "d") {
                                duration = "/Day"
                            }
                            else if(result.duration == "w") {
                                duration = "/Week"
                            }
                            else if(result.duration == "m") {
                                duration = "/Month"
                            }
                            else {
                                duration = "/Year"
                            }

                            val dec = DecimalFormat("#,###")
                            rentPrice.text = "Rp. "+ dec.format(result.price).toString() + duration

                            if (result.pictures.isEmpty()) {
                                rentPicture.setImageResource(R.drawable.house_placeholder)
                            }
                            else{
                                Picasso.get().load(result.pictures).into(rentPicture)
                            }
                        }
                    }
                }
                override fun onCancelled(p0: DatabaseError) {
                    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                }
            })
        }
    }
}