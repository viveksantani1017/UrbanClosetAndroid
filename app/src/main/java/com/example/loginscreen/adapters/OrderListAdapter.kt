package com.example.loginscreen.adapters

import android.app.Activity
import android.net.Uri
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import com.example.loginscreen.R
import com.example.loginscreen.models.Order
import com.example.loginscreen.models.Product

class OrderListAdapter(
    private val activity: Activity,
    private val objects: Array<Order>
) : ArrayAdapter<Order>(activity, R.layout.order_list, objects) {
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var view: View? = convertView
        val viewHolder: ViewHolder

        if (view == null) {
            view = activity.layoutInflater.inflate(R.layout.order_list, parent, false)

            viewHolder = ViewHolder()
            viewHolder.status = view.findViewById(R.id.orderstatus)
            viewHolder.date = view.findViewById(R.id.orderdate)
            viewHolder.totalprice = view.findViewById(R.id.tvtotalprice)
            viewHolder.ordernumber = view.findViewById(R.id.ordernumber)

            view.tag = viewHolder
        } else {
            viewHolder = view.tag as ViewHolder
        }

        viewHolder.status.text = objects[position].orderstatus
        viewHolder.totalprice.text = objects[position].totalprice.toString()
        viewHolder.date.text = objects[position].date
        viewHolder.ordernumber.text = objects[position].ordernumber.toString()

        view?.contentDescription = objects[position].id.toString()

        return view!!
    }
    companion object
    {
        class ViewHolder
        {
            lateinit var status: TextView
            lateinit var totalprice: TextView
            lateinit var date: TextView
            lateinit var ordernumber: TextView
        }
    }
}