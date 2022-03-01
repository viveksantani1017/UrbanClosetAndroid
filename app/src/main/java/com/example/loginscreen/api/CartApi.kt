package com.example.loginscreen.api

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import com.example.loginscreen.models.Product
import com.example.loginscreen.models.Profile
import org.json.JSONObject
import java.io.File
import java.io.FileOutputStream
import java.lang.Exception
import java.net.HttpURLConnection
import java.net.URL

class CartApi {

    companion object {


        internal fun getAll(): Array<Profile> {
            val checkoutlist = arrayListOf<Profile>()

            val url = URL("${Productapi.API_URL}/addtocart?userid=10")
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
                        orderJson.getInt("Orderid"),
                        orderJson.getString("image"),
                        orderJson.getInt("TotalPrice"),
                        orderJson.getString("ProductName"),
                        orderJson.getString("Size"),
                        orderJson.getInt("Quantity"),
                        orderJson.getString("ProductPrice"),
                        orderJson.getString("Address")
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

        internal fun downloadImage(context: Context, product: Product) {
            for (image in product.images) {

                val url = URL("${Productapi.API_URL}/images/${image}")
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

                    val imageSavePath = FileOutputStream("${imageDirPath}${image}")
                    connection.connect()
                    if (connection.responseCode == HttpURLConnection.HTTP_OK) {
                        val bitmap = BitmapFactory.decodeStream(connection.inputStream)
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 80, imageSavePath)
                    }
                } catch (ex: Exception) {
                    Log.e("downloadImage", ex.message!!)
                } finally {
                    connection.disconnect()
                }
            }
        }

    }


}