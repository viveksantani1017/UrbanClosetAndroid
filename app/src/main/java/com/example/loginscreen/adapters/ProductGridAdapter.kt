package com.example.loginscreen.adapters

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat.startActivity
import com.example.loginscreen.R
import com.example.loginscreen.api.Productapi
import com.example.loginscreen.login
import com.example.loginscreen.models.Product
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ProductGridAdapter(
    private val activity: Activity,
    private val objects: Array<Product>
) : ArrayAdapter<Product>(activity, R.layout.product_grid, objects) {
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var view: View? = convertView
        val viewHolder: ViewHolder

        if (view == null) {
            view = activity.layoutInflater.inflate(R.layout.product_grid, parent, false)

            viewHolder = ViewHolder()
            viewHolder.name = view.findViewById(R.id.tvproductname)
            viewHolder.price = view.findViewById(R.id.tvproductprice)
            viewHolder.image = view.findViewById(R.id.productimage)
            viewHolder.categoryName = view.findViewById(R.id.tvcategoryname)
            viewHolder.wishlistIcon = view.findViewById(R.id.whislisticon)
            view.tag = viewHolder
        } else {
            viewHolder = view.tag as ViewHolder
        }
        viewHolder.wishlistIcon.setOnClickListener {
            val pref = context.getSharedPreferences("UrbanCloset", AppCompatActivity.MODE_PRIVATE)
            val userid = pref.getInt("UserID", 0)
            if (userid == 0) {
                Toast.makeText(activity, "Login To Add In Wishlist", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if (!objects[position].inwishlist) {
                CoroutineScope(Dispatchers.IO).launch {
                    val response = Productapi.addToWishlist(
                        activity as AppCompatActivity,
                        objects[position].id
                    )
                    if (response.getBoolean("status"))
                        withContext(Dispatchers.Main) {
                            viewHolder.wishlistIcon.setImageResource(R.drawable.ic_fav)
                            objects[position].inwishlist = true
                        }
                    else {
                        withContext(Dispatchers.Main) {
                            Toast.makeText(activity, "Login to add", Toast.LENGTH_SHORT).show()
                        }
                    }

                }
            } else {
                CoroutineScope(Dispatchers.IO).launch {
                    val response = Productapi.removeFromWishlist(
                        activity as AppCompatActivity,
                        objects[position].id
                    )
                    if (response.getBoolean("status"))
                        withContext(Dispatchers.Main) {
                            viewHolder.wishlistIcon.setImageResource(R.drawable.ic_fav_outlined)
                            objects[position].inwishlist = false
                        }
                }
            }
        }

        viewHolder.name.text = objects[position].name
        viewHolder.price.text = "₹"+objects[position].price.toString()
        viewHolder.categoryName.text = objects[position].categoryname
        viewHolder.image.setImageURI(Uri.parse("${activity.externalCacheDir}/images/${objects[position].images[0]}"))

        if (objects[position].inwishlist)
            viewHolder.wishlistIcon.setImageResource(R.drawable.ic_fav)
        else
            viewHolder.wishlistIcon.setImageResource(R.drawable.ic_fav_outlined)

        view?.contentDescription = objects[position].id.toString()

        return view!!
    }

    companion object {
        class ViewHolder {
            lateinit var name: TextView
            lateinit var price: TextView
            lateinit var categoryName: TextView
            lateinit var image: ImageView
            lateinit var wishlistIcon: ImageView
        }
    }
}