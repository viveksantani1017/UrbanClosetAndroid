package com.example.loginscreen.api

import android.app.Application
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.loginscreen.models.Product
import org.json.JSONArray
import org.json.JSONObject
import java.io.File
import java.io.FileOutputStream
import java.lang.Exception
import java.net.HttpURLConnection
import java.net.URL

class Productapi {

    companion object {
        val API_URL = "http://10.1.90.19:8084/UrbanClosetApache"

        internal fun getAll(id: Int,context: Context): Array<Product> {
            val pref = context.getSharedPreferences("UrbanCloset", AppCompatActivity.MODE_PRIVATE)
            val userId = pref.getInt("UserID", 0)
            val productList = arrayListOf<Product>()

            val url = URL("$API_URL/getproduct?catid=$id&userid=2")
            val connection = (url.openConnection() as HttpURLConnection).apply {
                requestMethod = "GET"
                doInput = true
                setRequestProperty("Content-Type", "application/json")
                setChunkedStreamingMode(0)
            }
            if (connection.responseCode == HttpURLConnection.HTTP_OK) {
                val reader = connection.inputStream.bufferedReader()
                val responseJson = JSONObject(reader.readText())
                val productsJson = responseJson.getJSONArray("Products")

                var i = 0
                while (i < productsJson.length()) {
                    val productJson = productsJson.getJSONObject(i)

                    var images = arrayOf(
                        productJson.getString("image"),
                        productJson.getString ("image2"),
                        productJson.getString ("image3")

                    )
                    val productdata = Product(
                        productJson.getInt("productid"),
                        productJson.getString("ProductName"),
                        productJson.getInt("ProductPrice"),
                        productJson.getString("ProductDescription"),
                        productJson.getInt("ProductQuantity"),
                        productJson.getString("ProductColour"),
                        productJson.getString("ProductSize"),
                        images,
                        productJson.getString ("CategoryName"),
                        productJson.getBoolean("inwishlist")
                    )
                    productList.add(productdata)

                    i++
                }
            }

            return productList.toTypedArray()
        }

        internal fun downloadImage(context: Context, product: Product) {
            for(image in product.images) {

                val url = URL("$API_URL/images/${image}")
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

        internal fun getProduct(id: Int): Product? {
            val productList = arrayListOf<Product>()
            val url = URL("$API_URL/getproductdetail?productid=$id")
            val connection = (url.openConnection() as HttpURLConnection).apply {
                requestMethod = "GET"
                doInput = true
                setRequestProperty("Content-Type", "application/json")
                setChunkedStreamingMode(0)
            }

            if (connection.responseCode == HttpURLConnection.HTTP_OK) {

                val reader = connection.inputStream.bufferedReader()
                val responseJson = JSONArray(reader.readText())
                val productsJson = responseJson.getJSONObject(0)
                var images = arrayOf(
                    productsJson.getString("image"),
                    productsJson.getString ("image2"),
                    productsJson.getString ("image3")

                )

                with(productsJson)
                {
                    return Product(
                        getInt("productid"),
                        getString("ProductName"),
                        getInt("ProductPrice"),
                        getString("ProductDescription"),
                        getInt("ProductQuantity"),
                        getString("ProductColour"),
                        getString("ProductSize"),
                        images,
                        getString("CategoryName"),
                        getBoolean("inwishlist")
                    )
                }
            }
            return null
        }
    }
}

