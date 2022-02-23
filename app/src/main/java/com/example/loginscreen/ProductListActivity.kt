package com.example.loginscreen

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.GridView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.loginscreen.adapters.ProductGridAdapter
import com.example.loginscreen.api.Productapi
import com.example.loginscreen.models.Category
import kotlinx.coroutines.*

class ProductListActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product_list)
        var actionBar = getSupportActionBar()
        val catintent = intent
        val catid = catintent.getIntExtra("CatId",0)
        actionBar?.setTitle(catintent.getStringExtra("CategoryName"))
        actionBar!!.setDisplayHomeAsUpEnabled(true)
        val grdProducts = findViewById<GridView>(R.id.grdProducts)


        CoroutineScope(Dispatchers.IO).launch {
            grdProducts.setOnItemClickListener { _, view, _, _ ->
                val productId = view.contentDescription.toString().toInt()
                val intent = Intent(this@ProductListActivity, DetailActivity::class.java)
                intent.putExtra("ProductID", productId)
                CoroutineScope(Dispatchers.Main).launch {
                    startActivity(intent)
                    finish()
                }
            }

            val products = Productapi.getAll(catid)
            if (products.isNotEmpty()) {

                for (product in products)
                    Productapi.downloadImage(this@ProductListActivity, product)

                val adapter = ProductGridAdapter(this@ProductListActivity, products)
                withContext(Dispatchers.Main) { grdProducts.adapter = adapter }
            } else {
                withContext(Dispatchers.Main) {
                    Toast.makeText(
                        this@ProductListActivity,
                        "Empty Product Array",
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




