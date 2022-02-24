package com.example.loginscreen

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.ImageView
import com.synnapps.carouselview.CarouselView
import android.widget.TextView
import androidx.core.app.NavUtils
import com.example.loginscreen.api.Productapi
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class productdetails : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_productdetails)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        var whishlist = findViewById<ImageView>(R.id.wishlist)
        CoroutineScope(Dispatchers.IO).launch {
            val Intent = intent
            val productId = Intent.getIntExtra("ProductID", 0)
            Productapi.getProduct(productId).also {

                if (it != null) {
                    Productapi.downloadImage(this@productdetails, it)
                    withContext(Dispatchers.Main) {
                        var imgslider = findViewById<CarouselView>(R.id.carouselView)

                        imgslider.setImageListener { position, imageView ->
                            imageView.scaleType = ImageView.ScaleType.FIT_CENTER
                            imageView.setImageURI(Uri.parse("${externalCacheDir?.absolutePath}/images/${it.images[position]}"))
                        }
                        imgslider.pageCount = it.images.size

                        findViewById<TextView>(R.id.productname).text = it.name
                        findViewById<TextView>(R.id.productdes).text = it.description
                        findViewById<TextView>(R.id.price).setText("₹" + it.price)

                    }
//                    whishlist.setOnClickListener{
//                    }
                }
            }
        }

    }
    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}