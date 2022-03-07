package com.example.loginscreen

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.GridView
import android.widget.TextView
import android.widget.Toast
import com.example.loginscreen.adapters.checkoutlistAdapter
import com.example.loginscreen.api.Productapi
import com.example.loginscreen.api.CheckoutApi
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
        supportActionBar!!.setTitle("Checkout")
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        val orderlist = findViewById<GridView>(R.id.orderlist)

        CoroutineScope(Dispatchers.IO).launch {
            val orders = CheckoutApi.getAll(this@checkout_Activity)
            val address = CheckoutApi.getExtraInfo(this@checkout_Activity)
            if (orders.isNotEmpty()) {
                val adapter = checkoutlistAdapter(this@checkout_Activity, orders)
                withContext(Dispatchers.Main) {
                    orderlist.adapter = adapter
                    findViewById<TextView>(R.id.address).text = address[0]
                }
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
        findViewById<Button>(R.id.btncheckout).setOnClickListener {
            CoroutineScope(Dispatchers.IO).launch {
                val extrainfo = CheckoutApi.getExtraInfo(this@checkout_Activity)
                val response = placeOrder(extrainfo.get(2).toInt())
                if (response.getBoolean("status")) {
                    withContext(Dispatchers.Main) {
                        Toast.makeText(this@checkout_Activity, "Order Placed", Toast.LENGTH_SHORT)
                            .show()
                        startActivity(Intent(this@checkout_Activity, OrderActivity::class.java))
                        finish()
                    }
                } else {
                    withContext(Dispatchers.Main) {
                        Toast.makeText(
                            this@checkout_Activity,
                            "Error In Placing Order",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
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

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}

