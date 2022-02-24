package com.example.loginscreen

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.GridView
import android.widget.ListView
import android.widget.Toast
import com.example.loginscreen.adapters.OrderListAdapter
import com.example.loginscreen.adapters.ProductGridAdapter
import com.example.loginscreen.api.OrderApi
import com.example.loginscreen.api.Productapi
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class OrderActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_order)
        var actionbar = supportActionBar
        actionbar?.setTitle("My orders")
        actionbar?.setDisplayHomeAsUpEnabled(true)
        val orderlist = findViewById<GridView>(R.id.orderlist)

        CoroutineScope(Dispatchers.IO).launch {
//            orderlist.setOnItemClickListener { _, view, _, _ ->
//                val productId = view.contentDescription.toString().toInt()
//                val intent = Intent(this@ProductListActivity, DetailActivity::class.java)
//                intent.putExtra("ProductID", productId)
//                CoroutineScope(Dispatchers.Main).launch {
//                    startActivity(intent)
//                    finish()
//                }
//            }
            val orders = OrderApi.getAll()
            if (orders.isNotEmpty()) {
                val adapter = OrderListAdapter(this@OrderActivity, orders)
                withContext(Dispatchers.Main) { orderlist.adapter = adapter }
            } else {
                withContext(Dispatchers.Main) {
                    Toast.makeText(
                        this@OrderActivity,
                        "Empty Order Array",
                        Toast.LENGTH_SHORT
                    )
                        .show()
                }
            }
        }

    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                finish()
                return true
            }
        }
        return super.onContextItemSelected(item)
    }
}