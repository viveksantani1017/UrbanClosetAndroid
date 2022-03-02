package com.example.loginscreen

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.GridView
import android.widget.Toast
import com.example.loginscreen.adapters.checkoutlistAdapter
import com.example.loginscreen.api.CartApi
import com.example.loginscreen.api.Productapi
import com.example.loginscreen.api.checkoutApi
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.lang.Exception
import java.net.HttpURLConnection
import java.net.URL

class checkout_Activity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_checkout)

        val orderlist = findViewById<GridView>(R.id.orderlist)

        CoroutineScope(Dispatchers.IO).launch {
            val orders = checkoutApi.getAll()
            if (orders.isNotEmpty()) {
                val adapter = checkoutlistAdapter(this@checkout_Activity, orders)
                withContext(Dispatchers.Main) { orderlist.adapter = adapter }
            } else {
                withContext(Dispatchers.Main) {
                    Toast.makeText(
                        this@checkout_Activity,
                        "Empty Order Array",
                        Toast.LENGTH_SHORT
                    )
                        .show()
                }
            }
        }


    }

    private fun placeOrder(id: Int): JSONObject {
        val url =
            URL("${Productapi.API_URL}/checkout?orderid=${id}")
        val connection = (url.openConnection() as HttpURLConnection).apply {
            requestMethod = "PUT"
            doInput = true
            setRequestProperty("Content-Type", "Application/json")
            setChunkedStreamingMode(0)
        }
        try {
            if (connection.responseCode == HttpURLConnection.HTTP_OK) {
//                reading the response data
                val reader = connection.inputStream.bufferedReader()
                val jsonResponseString = reader.readText()
                return JSONObject(jsonResponseString)
            }
        } catch (Ex: Exception) {
            Log.i("Add to Cart Error", Ex.message.toString())
        } finally {
            connection.disconnect()
        }
        return JSONObject()
    }
}




//CoroutineScope(Dispatchers.IO).launch {
//    val cartid = CartApi.getTotalPrice(this@CartActivity)
//    val response = placeOrder(cartid[1])
//    if(response.getBoolean("status"))
//    {
//        withContext(Dispatchers.Main){
//            Toast.makeText(this@CartActivity, response.getString("message"), Toast.LENGTH_SHORT).show()
//            val intent = Intent(this@CartActivity,checkout_Activity::class.java)
//            startActivity(intent)
//        }
//    }
//    else
//    {
//        withContext(Dispatchers.Main){
//            Toast.makeText(this@CartActivity, response.getString("message"), Toast.LENGTH_SHORT).show()
//        }
//    }
//}