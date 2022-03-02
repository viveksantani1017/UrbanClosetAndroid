package com.example.loginscreen.api

import android.util.Log
import com.example.loginscreen.models.Profile
import org.json.JSONObject
import java.net.HttpURLConnection
import java.net.URL

class checkoutApi {

    companion object {


        internal fun getAll(): Array<Profile> {
            val checkoutlist = arrayListOf<Profile>()

            val url = URL("${Productapi.API_URL}/addtocart?userid=2")
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
                    var orderdata = Profile(
                        orderJson.getInt("orderedproductid"),
                        orderJson.getString("image"),
                        orderJson.getInt("TotalPrice"),
                        orderJson.getString("ProductName"),
                        orderJson.getString("ProductSize"),
                        orderJson.getInt("ProductQuantity"),
                        orderJson.getString("ProductPrice"),
                        orderJson.getString("Address1")
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
    }
}