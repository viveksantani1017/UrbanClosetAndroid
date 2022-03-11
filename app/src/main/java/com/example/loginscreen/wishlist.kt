package com.example.loginscreen

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.GridView
import android.widget.TextView
import android.widget.Toast
import com.example.loginscreen.adapters.wishlistAdapter
import com.example.loginscreen.api.WishlistApi
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class wishlist : AppCompatActivity() {
    lateinit var tverrorinfo:TextView
    lateinit var btnlogin: Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_wishlist)
        tverrorinfo = findViewById(R.id.wishlisterrorinfo)
        btnlogin = findViewById(R.id.wishlistloginbtn)
        btnlogin.visibility = View.GONE
        var grid = findViewById<GridView>(R.id.wishlistgrid)
        val userid = getSharedPreferences("UrbanCloset", MODE_PRIVATE).getInt("UserID",0)
        if(userid == 0)
        {
            tverrorinfo.text = getString(R.string.loginerrorwishlist)
            grid.visibility = View.GONE
            btnlogin.visibility = View.VISIBLE
            btnlogin.setOnClickListener {
                startActivity(Intent(this,login::class.java))
                recreate()
            }
        }
        supportActionBar!!.title = "Your Wishlist"
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        CoroutineScope(Dispatchers.IO).launch {
            val orders = WishlistApi.getAll(userid)
            if (orders.isNotEmpty()) {
                for (product in orders) {
                    WishlistApi.downloadImage(this@wishlist, product)
                }
                val adapter = wishlistAdapter(this@wishlist, orders)
                withContext(Dispatchers.Main) { grid.adapter = adapter }
            } else {
                withContext(Dispatchers.Main) {
                    tverrorinfo.text = getString(R.string.emptyWishlist)
                    grid.visibility = View.GONE
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