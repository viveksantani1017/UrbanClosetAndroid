package com.example.loginscreen.api

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
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
        val API_URL = "http://192.168.1.1:8084/UrbanClosetApache"

        internal fun getAll(id:Int): Array<Product> {
            val productList = arrayListOf<Product>()

            val url = URL("$API_URL/getproduct?catid=$id")
            val connection = (url.openConnection() as HttpURLConnection).apply {
                requestMethod = "GET"
                doInput = true
                setRequestProperty("Content-Type", "application/json")
                setChunkedStreamingMode(0)
            }
            if (connection.responseCode == HttpURLConnection.HTTP_OK) {
                val reader = connection.inputStream.bufferedReader()
                val responseJson = JSONObject(reader.readText())
                val wishlistsJson = responseJson.getJSONArray("Products")

                var i = 0
                while (i < wishlistsJson.length()) {
                    val wishlistJson = wishlistsJson.getJSONObject(i)
                    val wishlistdata = Product(
                        wishlistJson.getInt("productid"),
                        wishlistJson.getString("ProductName"),
                        wishlistJson.getInt("ProductPrice"),
                        wishlistJson.getInt("ProductQuantity"),
                        wishlistJson.getString("ProductColour"),
                        wishlistJson.getString("ProductSize"),
                        wishlistJson.getString("image"),
                        wishlistJson.getString("image2"),
                        wishlistJson.getString("image3"),
                        wishlistJson.getString("CategoryName"),
                    )
                    productList.add(wishlistdata)

                    i++
                }
            }

            return productList.toTypedArray()
        }

        internal fun downloadImage(context: Context, product: Product) {
            val url = URL("$API_URL/images/${product.imagePath}")
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

                val imageSavePath = FileOutputStream("${imageDirPath}${product.imagePath}")

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

        internal fun getProduct(id: Int): Product? {
            val productList= arrayListOf<Product>()
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

                with(productsJson)
                {
                    return Product(
                        getInt("productid"),
                        getString("ProductName"),
                        getInt("ProductPrice"),
                        getInt("ProductQuantity"),
                        getString("ProductColour"),
                        getString("ProductSize"),
                        getString("image"),
                        getString("image2"),
                        getString("image3"),
                        getString("CategoryName")
                    )
                }
            }
                return null
        }
    }
}

