package com.example.loginscreen

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.*
import com.synnapps.carouselview.CarouselView
import androidx.core.app.NavUtils
import androidx.core.content.ContentProviderCompat.requireContext
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

        // get reference to the string array that we just created
        val languages = resources.getStringArray(R.array.size_menu)
        // create an array adapter and pass the required parameter
        // in our case pass the context, drop down layout , and array.
        val arrayAdapter = ArrayAdapter(this, R.layout.dropdown, languages)
        // get reference to the autocomplete text view
        val autocompleteTV = findViewById<AutoCompleteTextView>(R.id.autoComplete)
        // set adapter to the autocomplete tv to the arrayAdapter
        autocompleteTV.setAdapter(arrayAdapter)
        var whishlist = findViewById<ImageView>(R.id.wishlist)
        // Adding Product To Cart
        var btnaddtocart = findViewById<Button>(R.id.add_to_cart)
        var etquantity = findViewById<EditText>(R.id.etquantity)
        var tvprice = findViewById<TextView>(R.id.price)
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

                }
            }

            //Adding to cart by click event
            btnaddtocart.setOnClickListener {
                var quantity = etquantity.text
                var price = tvprice.text
                var size = autocompleteTV.text
                    Toast.makeText(this@productdetails, size.toString() , Toast.LENGTH_SHORT).show()
            }
        }


    }
    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}