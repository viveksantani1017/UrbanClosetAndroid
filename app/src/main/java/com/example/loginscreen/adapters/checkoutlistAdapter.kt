package com.example.loginscreen.adapters

import android.app.Activity
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.example.loginscreen.R
import com.example.loginscreen.models.checkout

class checkoutlistAdapter(

    private val activity: Activity,
    private val objects: Array<checkout>
) : ArrayAdapter<checkout>(activity, R.layout.checkoutlist_grid, objects) {
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var view: View? = convertView
        val viewHolder: ViewHolder

        if (view == null) {
            view = activity.layoutInflater.inflate(R.layout.checkoutlist_grid, parent, false)

            viewHolder = ViewHolder()
            viewHolder.image= view.findViewById(R.id.productimage)
            viewHolder.name = view.findViewById(R.id.productname)
            viewHolder.size = view.findViewById(R.id.sizevalue)
            viewHolder.price = view.findViewById(R.id.pricevalue)
            viewHolder.quantity = view.findViewById(R.id.quantityvalue)
            view.tag = viewHolder
        } else {
            viewHolder = view.tag as ViewHolder
        }

        viewHolder.image.text = objects[position].productimage
        viewHolder.name.text = objects[position].name
        viewHolder.size.text = objects[position].size
        viewHolder.price.text = objects[position].price
        viewHolder.quantity.text = objects[position].quantity.toString()

        view?.contentDescription = objects[position].id.toString()

        return view!!
    }

    companion object {
        class ViewHolder {
            lateinit var name: TextView
            lateinit var image: TextView
            lateinit var size: TextView
            lateinit var price: TextView
            lateinit var quantity: TextView
            lateinit var address: TextView
        }
    }
}