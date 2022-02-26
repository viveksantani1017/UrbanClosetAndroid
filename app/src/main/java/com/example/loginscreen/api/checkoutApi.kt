package com.example.loginscreen.api

import com.example.loginscreen.models.checkout
import org.json.JSONObject
import java.net.HttpURLConnection
import java.net.URL

class checkoutApi {

    companion object {
        val API_URL = "http://172.31.0.2:9993/UrbanClosetApache"

        internal fun getAll(): Array<checkout> {
            val checkoutlist = arrayListOf<checkout>()

            val url = URL("$API_URL/orderwisedetail?userid=2")
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
                    var orderdata = checkout(
                        orderJson.getInt("orderedproductid"),
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

            return checkoutlist.toTypedArray()
        }
    }
}