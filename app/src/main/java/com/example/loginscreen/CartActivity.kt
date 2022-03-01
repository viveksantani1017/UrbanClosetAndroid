package com.example.loginscreen

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.GridView
import android.widget.Toast
import com.example.loginscreen.adapters.cartGridAdapter
import com.example.loginscreen.api.CartApi
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class CartActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cart)

        val cartlist = findViewById<GridView>(R.id.cartgrid)

        CoroutineScope(Dispatchers.IO).launch {
            val orders = CartApi.getAll()
            if (orders.isNotEmpty()) {
                val adapter = cartGridAdapter(this@CartActivity, orders)
                withContext(Dispatchers.Main) { cartlist.adapter = adapter }
            } else {
                withContext(Dispatchers.Main) {
                    Toast.makeText(
                        this@CartActivity,
                        "Empty Cart",
                        Toast.LENGTH_SHORT
                    )
                        .show()
                }
            }
        }
    }
}