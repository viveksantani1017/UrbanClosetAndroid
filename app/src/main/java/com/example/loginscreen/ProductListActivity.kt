package com.example.loginscreen

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.GridView
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.loginscreen.adapters.ProductGridAdapter
import com.example.loginscreen.api.Productapi
import com.example.loginscreen.models.Category
import com.example.loginscreen.models.Product
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
        var icon = findViewById<ImageView>(R.id.icon)
        icon = null
        val uri = "@drawable/ic_fav"
        var imgres = resources.getIdentifier(uri,null,packageName)
        var res = resources.getDrawable(imgres)
        CoroutineScope(Dispatchers.IO).launch {
            grdProducts.setOnItemClickListener { _, view, _, _ ->
                val productId = view.contentDescription.toString().toInt()
                val intent = Intent(this@ProductListActivity, productdetails::class.java)
                intent.putExtra("ProductID", productId)
                CoroutineScope(Dispatchers.Main).launch {
                    startActivity(intent)

                }
            }

            val products = Productapi.getAll(catid,this@ProductListActivity)
            if (products.isNotEmpty()) {

                for (product in products){
                    Productapi.downloadImage(this@ProductListActivity, product)
                    if(product.inwishlist)
                    {
                       icon?.setBackgroundResource(R.drawable.ic_fav)
                    }
                }

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




