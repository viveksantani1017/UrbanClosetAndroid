package com.example.loginscreen

import android.os.Bundle
import android.widget.GridView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.loginscreen.adapters.ProductGridAdapter
import com.example.loginscreen.api.Productapi
import kotlinx.coroutines.*

class TestActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test)

        val grdProducts = findViewById<GridView>(R.id.grdProducts)
        grdProducts.setOnItemClickListener { _, view, _, _ ->
            val productId = view.contentDescription.toString().toInt()
            val product = Productapi.getProduct(productId)
        }

        CoroutineScope(Dispatchers.IO).launch {
            val products = Productapi.getAll()
            if (products.isNotEmpty()) {

                for (product in products)
                    Productapi.downloadImage(this@TestActivity, product)
                // val adapter = ProductListAdapter(this@MainActivity, products)
                // withContext(Dispatchers.Main) { lstProducts.adapter = adapter }


                val adapter = ProductGridAdapter(this@TestActivity, products)
                withContext(Dispatchers.Main) { grdProducts.adapter = adapter }
            } else {
                withContext(Dispatchers.Main) {
                    Toast.makeText(this@TestActivity, "Empty Product Array", Toast.LENGTH_SHORT)
                        .show()
                }
            }

        }


        //        var btnfetchdata = findViewById<Button>(R.id.btnfetchdata)
//
//        btnfetchdata.setOnClickListener {
//            CoroutineScope(Dispatchers.IO).launch {
//                var data = fetchProductData()
//                withContext(Dispatchers.Main){
//                    Toast.makeText(this@TestActivity, data, Toast.LENGTH_SHORT).show()
//                }
//            }
//        }
//        }
//    private fun fetchProductData(): String? {
//        val url = URL("http://10.1.90.19:8084/UrbanClosetApache/getproduct?catid=1")
//        val connection = (url.openConnection() as HttpURLConnection).apply {
//            requestMethod = "GET"
//            doInput = true
//            setRequestProperty("Content-Type", "application/json")
//            setChunkedStreamingMode(0)
//        }
//        try {
////            Creating json object to put data
//
////            Checking if url is read and response is sent or not
//            if (connection.responseCode == HttpURLConnection.HTTP_OK) {
////                reading the response data
//                val reader = connection.inputStream.bufferedReader()
//                val jsonResponseString = reader.readText()
//                val responseJson = JSONArray(jsonResponseString)
//                return responseJson.getJSONObject(1).getString("ProductName")
//            }
//        } catch (Ex: Exception) {
//            Log.e("Error", Ex.message.toString())
//        } finally {
//            connection.disconnect()
//        }
//        return null
    }
}



