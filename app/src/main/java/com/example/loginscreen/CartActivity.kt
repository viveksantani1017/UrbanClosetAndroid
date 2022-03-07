package com.example.loginscreen

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.GridView
import android.widget.TextView
import android.widget.Toast
import com.example.loginscreen.adapters.cartGridAdapter
import com.example.loginscreen.adapters.checkoutlistAdapter
import com.example.loginscreen.api.CartApi
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class CartActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cart)

        val cartlist = findViewById<GridView>(R.id.orderlist)
        supportActionBar!!.setTitle("Cart")
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        CoroutineScope(Dispatchers.IO).launch {
            val cartproducts = CartApi.getAll(this@CartActivity)

            if (cartproducts.isNotEmpty()) {
                val carttotal = CartApi.getTotalPrice(this@CartActivity)
                val adapter = cartGridAdapter(this@CartActivity, cartproducts)
                withContext(Dispatchers.Main) {
                    cartlist.adapter = adapter
                    findViewById<TextView>(R.id.totalpricevalue).text = "₹${carttotal[0]}"
                }
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
        findViewById<Button>(R.id.btnplaceorder).setOnClickListener {
                val intent = Intent(this,checkout_Activity::class.java)
                startActivity(intent)
        }
    }



    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}