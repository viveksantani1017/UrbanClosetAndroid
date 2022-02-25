package com.example.loginscreen.adapters

import android.app.Activity
import android.net.Uri
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import com.example.loginscreen.R
import com.example.loginscreen.cart
import com.example.loginscreen.models.Product
import com.example.loginscreen.models.orderdetails

class cartGridAdapter (

    private val activity: Activity,
    private val objects: Array<Product>
    ) : ArrayAdapter<Product>(activity, R.layout.cart_grid, objects) {
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var view: View? = convertView
        val viewHolder: ViewHolder

        if (view == null) {
            view = activity.layoutInflater.inflate(R.layout.cart_grid, parent, false)

            viewHolder = ViewHolder()
            viewHolder.image = view.findViewById(R.id.productimage)
            viewHolder.name = view.findViewById(R.id.productname)
            viewHolder.size = view.findViewById(R.id.sizevalue)
            viewHolder.totalprice = view.findViewById(R.id.totalpricevalue)
            viewHolder.quantity = view.findViewById(R.id.quantityvalue)
            view.tag = viewHolder
        } else {
            viewHolder = view.tag as ViewHolder
        }

        viewHolder.image.setImageURI(Uri.parse("${activity.externalCacheDir}/images/${objects[position].images[0]}"))
        viewHolder.name.text = objects[position].name
        viewHolder.size.text = objects[position].size
        viewHolder.totalprice.text = objects[position].price.toString()
        viewHolder.quantity.text = objects[position].quantity.toString()

        view?.contentDescription = objects[position].id.toString()

        return view!!
    }

    companion object {
        class ViewHolder {
            lateinit var name: TextView
            lateinit var productid: TextView
            lateinit var size: TextView
            lateinit var totalprice: TextView
            lateinit var quantity: TextView
            lateinit var image: ImageView
        }
    }
}