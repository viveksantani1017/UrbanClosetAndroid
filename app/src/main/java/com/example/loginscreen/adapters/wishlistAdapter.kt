package com.example.loginscreen.adapters

import android.app.Activity
import android.net.Uri
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import com.example.loginscreen.R
import com.example.loginscreen.models.wishlistmodel

class wishlistAdapter (

        private val activity: Activity,
        private val objects: Array<wishlistmodel>
    ) : ArrayAdapter<wishlistmodel>(activity, R.layout.wishlist_grid, objects) {
        override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
            var view: View? = convertView
            val viewHolder: ViewHolder

            if (view == null) {
                view = activity.layoutInflater.inflate(R.layout.wishlist_grid, parent, false)

                viewHolder = ViewHolder()
                viewHolder.image = view.findViewById(R.id.productimage)
                viewHolder.name = view.findViewById(R.id.productname)
                viewHolder.price = view.findViewById(R.id.pricevalue)

                viewHolder.name.isSelected = true

                view.tag = viewHolder
            } else {
                viewHolder = view.tag as ViewHolder
            }
            viewHolder.image.setImageURI(Uri.parse("${activity.externalCacheDir}/images/${objects[position].productimage[0]}"))
            viewHolder.name.text = objects[position].name
            viewHolder.price.text = objects[position].price.toString()

            view?.contentDescription = objects[position].id.toString()

            return view!!
        }

        companion object {
            class ViewHolder {
                lateinit var name: TextView
                lateinit var price: TextView
                lateinit var image: ImageView
            }
        }
    }

