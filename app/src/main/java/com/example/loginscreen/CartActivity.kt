package com.example.loginscreen

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.GridView
import android.widget.TextView
import android.widget.Toast
import com.example.loginscreen.adapters.cartGridAdapter
import com.example.loginscreen.api.CartApi
import com.example.loginscreen.api.Productapi
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.lang.Exception
import java.net.HttpURLConnection
import java.net.URL

class CartActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cart)

        val cartlist = findViewById<GridView>(R.id.cartgrid)

        CoroutineScope(Dispatchers.IO).launch {
            val cartproducts = CartApi.getAll(this@CartActivity)
            val carttotal = CartApi.getTotalPrice(this@CartActivity)
            if (cartproducts.isNotEmpty()) {
                val adapter = cartGridAdapter(this@CartActivity, cartproducts)
                withContext(Dispatchers.Main) {
                    cartlist.adapter = adapter
                    findViewById<TextView>(R.id.totalprice).text = "₹${carttotal[0]}"
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
        findViewById<Button>(R.id.btncheckout).setOnClickListener {
                val intent = Intent(this,checkout_Activity::class.java)
                startActivity(intent)
        }
    }



}