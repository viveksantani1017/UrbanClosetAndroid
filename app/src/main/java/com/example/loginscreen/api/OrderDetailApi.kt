package com.example.loginscreen.api

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import com.example.loginscreen.models.Product
import com.example.loginscreen.models.orderdetails
import org.json.JSONObject
import java.io.File
import java.io.FileOutputStream
import java.net.HttpURLConnection
import java.net.URL

class OrderDetailApi {

    companion object {
        val API_URL = "http://192.168.1.1:8084/UrbanClosetApache"

        internal fun getAll(id:Int): Array<orderdetails> {
            val productList = arrayListOf<orderdetails>()

            val url = URL("$API_URL/orderwisedetail?userid=2&orderid=${id}")
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
                    var orderdata = orderdetails(
                        orderJson.getInt("orderedproductid"),
                        orderJson.getString("image"),
                        orderJson.getString("ProductName"),
                        orderJson.getString("Size"),
                        orderJson.getString("Quantity"),
                        orderJson.getString("Address1"),
                        orderJson.getInt("TotalPrice")
                    )
                    productList.add(orderdata)

                    i++
                }
            }

            return productList.toTypedArray()
        }

        internal fun downloadImage(context: Context, product: orderdetails) {
            val url = URL("$API_URL/images/${product.productimage}")
            val connection = (url.openConnection() as HttpURLConnection).apply {
                requestMethod = "GET"
                doInput = true
            }

            try {
                val cacheDirPath = context.externalCacheDir!!.absolutePath
                val imageDirPath = "${cacheDirPath}/images/"

                val imageDir = File(imageDirPath)
                if (!imageDir.exists())
                    imageDir.mkdirs()

                val imageSavePath = FileOutputStream("${imageDirPath}${product.productimage}")

                connection.connect()
                if (connection.responseCode == HttpURLConnection.HTTP_OK) {
                    val bitmap = BitmapFactory.decodeStream(connection.inputStream)
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 30, imageSavePath)
                }
            } catch (ex: Exception) {
                Log.e("downloadImage", ex.message!!)
            } finally {
                connection.disconnect()
            }
        }
    }

}