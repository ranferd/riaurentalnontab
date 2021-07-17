package com.farid.riaurental.Adapter

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import com.farid.riaurental.DetailRentActivity
import com.farid.riaurental.Model.RentModel
import com.farid.riaurental.R
import com.squareup.picasso.Picasso
import java.text.DecimalFormat

@Suppress("UNREACHABLE_CODE", "NAME_SHADOWING")
class RentAdapter(val mCtx: Context, val layoutResId: Int, val list: List<RentModel>) : ArrayAdapter<RentModel>(mCtx,layoutResId,list){

    @SuppressLint("ViewHolder")
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val layoutInflater: LayoutInflater = LayoutInflater.from(mCtx)
        val view: View = layoutInflater.inflate(layoutResId,null)

        val rent = list[position]

        val textName = view.findViewById<TextView>(R.id.textName)
        val textAddress = view.findViewById<TextView>(R.id.textAddress)
        val textPrice = view.findViewById<TextView>(R.id.textPrice)
        val textDuration = view.findViewById<TextView>(R.id.textDuration)
        val imageView = view.findViewById<ImageView>(R.id.imageView)

        textName.text = rent.name
        textAddress.text = rent.address

        if (rent.duration == "d") {
            textDuration.text = "/day"
        }
        else if(rent.duration == "w") {
            textDuration.text = "/week"
        }
        else if(rent.duration == "m") {
            textDuration.text = "/month"
        }
        else {
            textDuration.text = "/year"
        }

        val dec = DecimalFormat("#,###")
        textPrice.text = "Rp "+ dec.format(rent.price).toString()

        if (rent.pictures.isEmpty()) {
            imageView.setImageResource(R.drawable.house_placeholder)
        }
        else{
            Picasso.get().load(rent.pictures.first().toString()).into(imageView)
        }

        textName.setOnClickListener {
            showDetail(rent)
        }

        return view
    }

    private fun showDetail(rent: RentModel) {
        val intent = Intent(context, DetailRentActivity::class.java)
        intent.putExtra("rentId", rent.id)
        context.startActivity(intent)
    }
}