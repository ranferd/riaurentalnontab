package com.farid.riaurental

import android.content.Intent
import android.os.Bundle
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity
import com.farid.riaurental.Adapter.RentAdapter
import com.farid.riaurental.Model.RentModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.ktx.Firebase
import com.google.firebase.firestore.ktx.firestore
import kotlinx.android.synthetic.main.activity_tenant_home.*

class TenantHomeActivity : AppCompatActivity() {

    private val db = Firebase.firestore
    private lateinit var auth: FirebaseAuth
    private lateinit var list : MutableList<RentModel>
    private lateinit var listView: ListView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tenant_home)

        auth = FirebaseAuth.getInstance()
        list = mutableListOf()
        listView = findViewById(R.id.listView)

        db.collection("rents")
            .addSnapshotListener { snapshot, e ->
                if(snapshot != null){
                    list.clear()
                    for (document in snapshot) {
                        val rent = document.toObject(RentModel::class.java)
                        list.add(rent)
                    }
                    val adapter = RentAdapter(this@TenantHomeActivity,R.layout.rent_item,list)
                    listView.adapter = adapter
                }
            }
    }

    //sign out method
//    fun signOut() {
//        auth.signOut()
//        finishAffinity()
//        startActivity(Intent(this, MainActivity::class.java))
//    }
}