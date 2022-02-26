package com.example.loginscreen.adapters

import android.app.Activity
import android.net.Uri
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import com.example.loginscreen.R
import com.example.loginscreen.models.Orderdetails

class OrderDetailAdapter(
        private val activity: Activity,
        private val objects: Array<Orderdetails>
    ) : ArrayAdapter<Orderdetails>(activity, R.layout.orderdetails_grid, objects)
    {
        override fun getView(position: Int, convertView: View?, parent: ViewGroup): View
        {
            var view: View? = convertView
            val viewHolder: ViewHolder

            if (view == null)
            {
                view = activity.layoutInflater.inflate(R.layout.orderdetails_grid, parent, false)

                viewHolder = ViewHolder()
                viewHolder.image = view.findViewById(R.id.productimage)
                viewHolder.name = view.findViewById(R.id.productname)
                viewHolder.size = view.findViewById(R.id.sizevalue)
                viewHolder.totalprice = view.findViewById(R.id.totalpricevalue)
                viewHolder.quantity = view.findViewById(R.id.quantityvalue)
                view.tag = viewHolder
            }
            else
            {
                viewHolder = view.tag as ViewHolder
            }

            viewHolder.image.setImageURI(Uri.parse("${activity.externalCacheDir}/images/${objects[position].productimage}"))
            viewHolder.name.text = objects[position].productName
            viewHolder.size.text = objects[position].size
            viewHolder.totalprice.text = objects[position].totalPrice.toString()
            viewHolder.quantity.text = objects[position].quantity

            view?.contentDescription = objects[position].id.toString()

            return view!!
        }

        companion object
        {
            class ViewHolder
            {
                lateinit var name: TextView
                lateinit var size: TextView
                lateinit var totalprice: TextView
                lateinit var quantity: TextView
                lateinit var image: ImageView
            }
        }
    }
