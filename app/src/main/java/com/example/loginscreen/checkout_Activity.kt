package com.example.loginscreen

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.GridView
import android.widget.Toast
import com.example.loginscreen.adapters.checkoutlistAdapter
import com.example.loginscreen.api.checkoutApi
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class checkout_Activity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_checkout)

        val orderlist = findViewById<GridView>(R.id.orderlist)

        CoroutineScope(Dispatchers.IO).launch {
            val orders = checkoutApi.getAll()
            if (orders.isNotEmpty()) {
                val adapter = checkoutlistAdapter(this@checkout_Activity, orders)
                withContext(Dispatchers.Main) { orderlist.adapter = adapter }
            } else {
                withContext(Dispatchers.Main) {
                    Toast.makeText(
                        this@checkout_Activity,
                        "Empty Order Array",
                        Toast.LENGTH_SHORT
                    )
                        .show()
                }
            }
        }


    }
}