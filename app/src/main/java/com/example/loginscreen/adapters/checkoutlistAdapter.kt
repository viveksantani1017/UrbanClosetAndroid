package com.example.loginscreen.adapters

import android.app.Activity
import android.net.Uri
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.example.loginscreen.R
import com.example.loginscreen.models.Checkout

class checkoutlistAdapter(

    private val activity: Activity,
    private val objects: Array<Checkout>
) : ArrayAdapter<Checkout>(activity, R.layout.cart_grid, objects) {
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var view: View? = convertView
        val viewHolder: ViewHolder

        if (view == null) {
            view = activity.layoutInflater.inflate(R.layout.cart_grid, parent, false)
            viewHolder = ViewHolder()
            viewHolder.image = view.findViewById(R.id.productimage)
            viewHolder.name = view.findViewById(R.id.productname)
            viewHolder.size = view.findViewById(R.id.sizevalue)
            viewHolder.price = view.findViewById(R.id.pricevalue)
            viewHolder.quantity = view.findViewById(R.id.quantityvalue)
            viewHolder.btnupdate =  view.findViewById(R.id.btn_update_cart)
            viewHolder.btnremove =  view.findViewById(R.id.btn_delete)
            viewHolder.name.isSelected = true

            view.tag = viewHolder

        } else {
            viewHolder = view.tag as ViewHolder
        }

        viewHolder.image.setImageURI(Uri.parse("${activity.externalCacheDir}/images/${objects[position].productimage}"))
        viewHolder.name.text = objects[position].name
        viewHolder.size.text = objects[position].size
        viewHolder.price.text = "₹"+objects[position].price
        viewHolder.quantity.text = objects[position].quantity.toString()
        view?.contentDescription = objects[position].id.toString()
        viewHolder.btnupdate.visibility = View.GONE
        viewHolder.btnremove.visibility = View.GONE
        return view!!
    }

    companion object {
        class ViewHolder {
            lateinit var name: TextView
            lateinit var size: TextView
            lateinit var price: TextView
            lateinit var quantity: TextView
            lateinit var image: ImageView
            lateinit var btnupdate: TextView
            lateinit var btnremove: TextView
        }
    }
}