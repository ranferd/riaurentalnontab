package com.farid.riaurental

import android.content.Intent
import android.os.Bundle
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity
import com.farid.riaurental.Adapter.RentAdapter
import com.farid.riaurental.Model.RentModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_tenant_home.*

class TenantHomeActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    lateinit var ref : DatabaseReference
    lateinit var list : MutableList<RentModel>
    lateinit var listView: ListView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tenant_home)

        auth = FirebaseAuth.getInstance()
        ref = FirebaseDatabase.getInstance().getReference("rents")
        list = mutableListOf()
        listView = findViewById(R.id.listView)

        ref.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()){
                    list.clear()
                    for (data in snapshot.children){
                        val rent = data.getValue(RentModel::class.java)

                        list.add(rent!!)
                    }
                    val adapter = RentAdapter(this@TenantHomeActivity,R.layout.rent_item,list)
                    listView.adapter = adapter
                }
            }
        })
    }

    //sign out method
    fun signOut() {
        auth.signOut()
        finishAffinity()
        startActivity(Intent(this, MainActivity::class.java))
    }
}