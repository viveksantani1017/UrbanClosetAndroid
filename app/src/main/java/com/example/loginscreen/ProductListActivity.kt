package com.example.loginscreen

import android.os.Bundle
import android.view.MenuItem
import android.widget.GridView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.loginscreen.adapters.ProductGridAdapter
import com.example.loginscreen.api.Productapi
import kotlinx.coroutines.*

class ProductListActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product_list)
        var actionBar = getSupportActionBar()
        actionBar!!.setDisplayHomeAsUpEnabled(true)
        val grdProducts = findViewById<GridView>(R.id.grdProducts)
        grdProducts.setOnItemClickListener { _, view, _, _ ->
            val productId = view.contentDescription.toString().toInt()
            val product = Productapi.getProduct(productId)
        }

        CoroutineScope(Dispatchers.IO).launch {
            val products = Productapi.getAll()
            if (products.isNotEmpty()) {

                for (product in products)
                    Productapi.downloadImage(this@ProductListActivity, product)

                val adapter = ProductGridAdapter(this@ProductListActivity, products)
                withContext(Dispatchers.Main) { grdProducts.adapter = adapter }
            } else {
                withContext(Dispatchers.Main) {
                    Toast.makeText(this@ProductListActivity, "Empty Product Array", Toast.LENGTH_SHORT)
                        .show()
                }
            }
        }
    }
    override fun onContextItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                finish()
                return true
            }
        }
        return super.onContextItemSelected(item)
    }
}




