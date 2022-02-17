package com.example.loginscreen.adapters

import android.app.Activity
import android.net.Uri
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import com.example.loginscreen.R
import com.example.loginscreen.models.Category

class CategoryGridAdapter(
    private val activity: Activity,
    private val objects: Array<Category>
):ArrayAdapter<Category>(activity, R.layout.product_grid,objects)
{
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var view: View? = convertView
        val viewHolder: ViewHolder

        if (view == null)
        {
            view = activity.layoutInflater.inflate(R.layout.product_grid, parent, false)

            viewHolder = ViewHolder()
            viewHolder.name = view.findViewById(R.id.tvproductname)
            viewHolder.image = view.findViewById(R.id.productimage)
            view.tag = viewHolder
        }
        else
        {
            viewHolder = view.tag as ViewHolder
        }

        viewHolder.name.text = objects[position].name
        viewHolder.image.setImageURI(Uri.parse("${activity.externalCacheDir}/images/${objects[position].imagePath}"))

        view?.contentDescription = objects[position].id.toString()

        return view!!
    }

    companion object
    {
        class ViewHolder
        {
            lateinit var name: TextView
            lateinit var image: ImageView
        }
    }

}