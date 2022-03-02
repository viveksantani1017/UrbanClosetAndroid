package com.example.loginscreen.api

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.loginscreen.models.Product
import com.example.loginscreen.models.checkout
import org.json.JSONObject
import java.io.File
import java.io.FileOutputStream
import java.lang.Exception
import java.net.HttpURLConnection
import java.net.URL

class CartApi {

    companion object {


        internal fun getAll(context: Context): Array<checkout> {
            val checkoutlist = arrayListOf<checkout>()
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
                    val orderdata = checkout(
                        orderJson.getInt("orderedproductid"),
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
            } else {
                Log.i("error", "Hello")
            }

            return checkoutlist.toTypedArray()
        }

        internal fun downloadImage(context: Context, product: Product) {


            val url = URL("${Productapi.API_URL}/images/${product.images[0]}")
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

                val imageSavePath = FileOutputStream("${imageDirPath}${product.images[0]}")
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

        internal fun getTotalPrice(context: Context): ArrayList<Int> {
            val userid = context.getSharedPreferences("UrbanCloset", AppCompatActivity.MODE_PRIVATE)
                .getInt("UserID", 0)
            val responseArray = arrayListOf<Int>()
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
                responseArray.add(orderJson.getInt("totalPrice"))
                responseArray.add(orderJson.getInt("Orderid"))
                return responseArray
            } else {
                Log.i("error", "Some Error")
            }

            return responseArray
        }
    }
}