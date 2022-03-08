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
import com.example.loginscreen.api.Productapi
import com.example.loginscreen.models.NewProductModel
import com.example.loginscreen.models.Product
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class NewProductAdapter (
    private val activity: Activity,
    private val objects: Array<NewProductModel>
) : ArrayAdapter<NewProductModel>(activity, R.layout.product_grid, objects) {
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
        viewHolder.image.setOnClickListener {
            val intent = Intent(activity, ProductDetails::class.java)
            intent.putExtra("ProductID", objects[position].id)
            ContextCompat.startActivity(context,intent, null)
        }
        viewHolder.name.text = objects[position].name
        viewHolder.price.text = "₹"+objects[position].price.toString()
        viewHolder.categoryName.text = objects[position].categoryName
        viewHolder.image.setImageURI(Uri.parse("${activity.externalCacheDir}/images/${objects[position].image}"))

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