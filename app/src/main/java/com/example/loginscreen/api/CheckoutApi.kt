package com.example.loginscreen.api

import android.content.Context
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.loginscreen.models.Checkout
import org.json.JSONObject
import java.net.HttpURLConnection
import java.net.URL

class CheckoutApi {
    companion object {
        internal fun getAll(context:Context): Array<Checkout> {
            val checkoutlist = arrayListOf<Checkout>()
            val userid = context.getSharedPreferences("UrbanCloset", AppCompatActivity.MODE_PRIVATE)
                .getInt("UserID", 0)
            val url = URL("${Productapi.API_URL}/addtocart?userid=${userid}")
            val connection = (url.openConnection() as HttpURLConnection).apply {
                requestMethod = "GET"
                doInput = true
                setRequestProperty("Content-Type", "application/json")
                setChunkedStreamingMode(0)
            }
            if (connection.responseCode == HttpURLConnection.HTTP_OK) {
                val reader = connection.inputStream.bufferedReader()
                val orderJson = JSONObject(reader.readText())
                val orderJsonArray = orderJson.getJSONArray("items")

                var i = 0
                while (i < orderJsonArray.length()) {
                    val orderJson = orderJsonArray.getJSONObject(i)
                    val orderdata = Checkout(
                        orderJson.getInt("orderedproductid"),
                        orderJson.getString("ProductName"),
                        orderJson.getString("image"),
                        orderJson.getString("Size"),
                        orderJson.getString("TotalPrice"),
                        orderJson.getInt("Quantity"),
                        orderJson.getInt("productid")
                    )
                    checkoutlist.add(orderdata)
                    i++
                }
            }
            else{
                Log.i("error","Hello")
            }

            return checkoutlist.toTypedArray()
        }
        internal fun getExtraInfo(context: Context): ArrayList<String> {
            val userid = context.getSharedPreferences("UrbanCloset", AppCompatActivity.MODE_PRIVATE)
                .getInt("UserID", 0)
            val responseArray = arrayListOf<String>()
            val url = URL("${Productapi.API_URL}/addtocart?userid=${userid}")
            val connection = (url.openConnection() as HttpURLConnection).apply {
                requestMethod = "GET"
                doInput = true
                setRequestProperty("Content-Type", "application/json")
                setChunkedStreamingMode(0)
            }
            if (connection.responseCode == HttpURLConnection.HTTP_OK) {
                val reader = connection.inputStream.bufferedReader()
                val orderJson = JSONObject(reader.readText())
                responseArray.add(orderJson.getString("Address"))
                responseArray.add(orderJson.getString("totalPrice"))
                responseArray.add(orderJson.getString("Orderid"))
                return responseArray
            } else {
                Log.i("error", "Some Error")
            }

            return responseArray
        }
    }
}