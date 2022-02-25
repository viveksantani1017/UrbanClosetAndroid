package com.example.loginscreen

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.GridView
import android.widget.Toast
import com.example.loginscreen.adapters.OrderDetailAdapter
import com.example.loginscreen.adapters.ProductGridAdapter
import com.example.loginscreen.api.OrderDetailApi
import com.example.loginscreen.api.Productapi
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class orderDetailActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_order_detail)
        supportActionBar?.setTitle("Order Detail")
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)


        val grdProducts = findViewById<GridView>(R.id.card)
        CoroutineScope(Dispatchers.IO).launch {
            val Intent = intent
            val orderid = Intent.getIntExtra("OrderId", 0)
            val orderedproducts =  OrderDetailApi.getAll(orderid)
            if (orderedproducts.isNotEmpty()) {


                    val adapter = OrderDetailAdapter(this@orderDetailActivity, orderedproducts)
                    withContext(Dispatchers.Main) { grdProducts.adapter = adapter }
                }

            else {
                withContext(Dispatchers.Main) {
                    Toast.makeText(
                        this@orderDetailActivity,
                        "Empty OrderDetail Array",
                        Toast.LENGTH_SHORT
                    )
                        .show()
                }
            }
        }
    }
    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}