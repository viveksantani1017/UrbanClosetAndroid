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
import androidx.core.content.ContextCompat
import com.example.loginscreen.ProductDetails
import com.example.loginscreen.R
import com.example.loginscreen.api.WishlistApi
import com.example.loginscreen.models.wishlistmodel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class wishlistAdapter(

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
            viewHolder.categoryName = view.findViewById(R.id.categoryname)
            viewHolder.btndelete = view.findViewById(R.id.btn_delete)

            viewHolder.name.isSelected = true

            view.tag = viewHolder
        } else {
            viewHolder = view.tag as ViewHolder
        }
        viewHolder.btndelete.setOnClickListener {
            val userid = context.getSharedPreferences("UrbanCloset", AppCompatActivity.MODE_PRIVATE)
                .getInt("UserID", 0)
            CoroutineScope(Dispatchers.IO).launch {
                val response = WishlistApi.deletewishlist(userid, objects[position].id)
                if (response.getBoolean("status")) {
                    withContext(Dispatchers.Main) {
                        Toast.makeText(activity, response.getString("message"), Toast.LENGTH_SHORT)
                            .show()
                        activity.recreate()
                    }
                } else {
                    withContext(Dispatchers.Main) {
                        Toast.makeText(activity, response.getString("message"), Toast.LENGTH_SHORT)
                            .show()
                    }
                }
            }
        }
        viewHolder.image.setOnClickListener {
            val intent = Intent(activity, ProductDetails::class.java)
            intent.putExtra("ProductID", objects[position].id)
            ContextCompat.startActivity(context,intent, null)
        }
        viewHolder.image.setImageURI(Uri.parse("${activity.externalCacheDir}/images/${objects[position].productimage}"))
        viewHolder.name.text = objects[position].name
        viewHolder.price.text = "₹"+objects[position].price
        viewHolder.categoryName.text = objects[position].categoryName

        view?.contentDescription = objects[position].id.toString()

        return view!!
    }

    companion object {
        class ViewHolder {
            lateinit var name: TextView
            lateinit var price: TextView
            lateinit var image: ImageView
            lateinit var categoryName: TextView
            lateinit var btndelete: TextView
        }
    }
}

