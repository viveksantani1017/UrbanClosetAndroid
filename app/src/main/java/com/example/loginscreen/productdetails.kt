package com.example.loginscreen

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.*
import com.synnapps.carouselview.CarouselView
import com.example.loginscreen.api.Productapi
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.lang.Exception
import java.net.HttpURLConnection
import java.net.URL


class productdetails : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_productdetails)

        supportActionBar?.title = "Product Detail"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val pref = getSharedPreferences("UrbanCloset", MODE_PRIVATE)
        val userid = pref.getInt("UserID",0)

        val productId = intent.getIntExtra("ProductID", 0)
        val sizes = resources.getStringArray(R.array.size_menu)
        val arrayAdapter = ArrayAdapter(this, R.layout.dropdown, sizes)
        val autocompleteTV = findViewById<AutoCompleteTextView>(R.id.autoComplete)
        autocompleteTV.setAdapter(arrayAdapter)

        var whishlist = findViewById<ImageView>(R.id.wishlist)
        val btnaddtocart = findViewById<Button>(R.id.add_to_cart)
        var etquantity = findViewById<EditText>(R.id.etquantity)
        val tvprice = findViewById<TextView>(R.id.price)


        btnaddtocart.setOnClickListener {
            val quantity = etquantity.text.toString()
            val price = tvprice.text.toString()
            val size = autocompleteTV.text.toString()
            CoroutineScope(Dispatchers.IO).launch{
                val response = addToCart(price, quantity, size, userid, productId)
                if(response.getBoolean("status"))
                {
                    withContext(Dispatchers.Main){
                        Toast.makeText(this@productdetails, response.getString("message"), Toast.LENGTH_SHORT).show()
                    }
                }
                else
                {
                    withContext(Dispatchers.Main){
                    MaterialAlertDialogBuilder(this@productdetails)
                        .setTitle("Log In To Add Product In Cart")
                        .setNeutralButton("cancel") { dialog, which ->
                            closeContextMenu()
                        }
                        .setPositiveButton("Login") { dialog, which ->
                            val intent = Intent(this@productdetails,login::class.java)
                            startActivity(intent)
                        }
                        .show()
                    }
                }
            }
        }

        CoroutineScope(Dispatchers.IO).launch {
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
                        tvprice.text = "₹${it.price}"
                    }
                }
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    private fun addToCart(
        price: String,
        quantity: String,
        size: String,
        userid: Int,
        productid: Int
    ): JSONObject {
        val url =
            URL("${Productapi.API_URL}/addtocart?productid=${productid}&userid=${userid}&quantity=${quantity}&size=${size}&price=${price}")
        val connection = (url.openConnection() as HttpURLConnection).apply {
            requestMethod = "POST"
            doInput = true
            setRequestProperty("Content-Type", "Application/json")
            setChunkedStreamingMode(0)
        }

        try {
            if (connection.responseCode == HttpURLConnection.HTTP_OK) {
//                reading the response data
                val reader = connection.inputStream.bufferedReader()
                val jsonResponseString = reader.readText()
                val responseJson = JSONObject(jsonResponseString)
                return responseJson
            }
        } catch (Ex: Exception) {
            Log.i("Add to Cart Error", Ex.message.toString())
        } finally {
            connection.disconnect()
        }
        return JSONObject()
    }

}