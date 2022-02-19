package com.example.loginscreen

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.loginscreen.api.Productapi
import com.example.loginscreen.models.Product
import android.content.Intent
import android.widget.ImageView
import android.widget.TextView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class DetailActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)
        CoroutineScope(Dispatchers.IO).launch {
            val Intent = intent
            val productId = Intent.getIntExtra("ProductID", 0)
            val product = Productapi.getProduct(productId)
            withContext(Dispatchers.Main) {
                var tv = findViewById<TextView>(R.id.textView)
                var image = findViewById<ImageView>(R.id.imageView)
                if (product != null) {
                    tv.text = product.name
                }

            }
        }


    }
}