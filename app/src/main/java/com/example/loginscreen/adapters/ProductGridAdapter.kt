package com.example.loginscreen.adapters

import android.app.Activity
import android.graphics.drawable.Drawable
import android.net.Uri
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.example.loginscreen.ProductListActivity
import com.example.loginscreen.R
import com.example.loginscreen.models.Product

class ProductGridAdapter(
    private val activity: Activity,
    private val objects: Array<Product>
) : ArrayAdapter<Product>(activity, R.layout.product_grid, objects)
{
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View
    {
        var view: View? = convertView
        val viewHolder: ViewHolder

        if (view == null)
        {
            view = activity.layoutInflater.inflate(R.layout.product_grid, parent, false)

            viewHolder = ViewHolder()
            viewHolder.name = view.findViewById(R.id.tvproductname)
            viewHolder.price = view.findViewById(R.id.tvproductprice)
            viewHolder.image = view.findViewById(R.id.productimage)
            viewHolder.catname = view.findViewById(R.id.tvcategoryname)
//            viewHolder.icon = view.findViewById(R.id.icon)
            view.tag = viewHolder
        }
        else
        {
            viewHolder = view.tag as ViewHolder
        }

        viewHolder.name.text = objects[position].name
        viewHolder.price.text = objects[position].price.toString()
        viewHolder.catname.text = objects[position].categoryname
        viewHolder.image.setImageURI(Uri.parse("${activity.externalCacheDir}/images/${objects[position].images[0]}"))
//        viewHolder.icon?.setImageResource(R.drawable.ic_fav_outlined)
//        if(objects[position].inwishlist)
//        {
//            viewHolder.icon.setImageResource(R.drawable.ic_fav)
//        }


        view?.contentDescription = objects[position].id.toString()

        return view!!
    }

    companion object
    {
        class ViewHolder
        {
            lateinit var name: TextView
            lateinit var price: TextView
            lateinit var catname: TextView
            lateinit var image: ImageView
//            var icon: ImageView? = null
        }
    }
}