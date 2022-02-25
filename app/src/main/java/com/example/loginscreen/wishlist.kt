package com.example.loginscreen

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.GridView
import android.widget.Toast
import com.example.loginscreen.adapters.wishlistAdapter
import com.example.loginscreen.api.WishlistApi
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class wishlist : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_wishlist)
        var grid = findViewById<GridView>(R.id.wishlistfgrid)
        CoroutineScope(Dispatchers.IO).launch {
            val orders = WishlistApi.getAll(this@wishlist)
            if (orders.isNotEmpty()) {
                val adapter = wishlistAdapter(this@wishlist, orders)
                withContext(Dispatchers.Main) { grid.adapter = adapter }
            } else {
                withContext(Dispatchers.Main) {
                    Toast.makeText(
                        this@wishlist,
                        "Empty Order Array",
                        Toast.LENGTH_SHORT
                    )
                        .show()
                }
            }
        }
    }
}