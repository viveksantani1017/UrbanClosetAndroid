package com.example.loginscreen

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
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
        var grid = findViewById<GridView>(R.id.wishlistgrid)
        supportActionBar!!.title = "Your Wishlist"
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        CoroutineScope(Dispatchers.IO).launch {
            val orders = WishlistApi.getAll()
            if (orders.isNotEmpty()) {
                for (product in orders) {
                    WishlistApi.downloadImage(this@wishlist, product)
                }
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