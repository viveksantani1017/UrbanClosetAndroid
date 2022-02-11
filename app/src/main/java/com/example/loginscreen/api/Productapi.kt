package com.example.loginscreen.api

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import com.example.loginscreen.models.Product
import org.json.JSONObject
import java.io.File
import java.io.FileOutputStream
import java.lang.Exception
import java.net.HttpURLConnection
import java.net.URL

class Productapi {
    companion object
    {
        internal fun getAll(): Array<Product>
        {
            val productList = arrayListOf<Product>()

            val url = URL("http://192.168.134.37:8084/UrbanClosetApache/getproduct?catid=1")
            val connection = (url.openConnection() as HttpURLConnection).apply {
                requestMethod = "GET"
                doInput = true
                setRequestProperty("Content-Type", "application/json")
                setChunkedStreamingMode(0)
            }

            if (connection.responseCode == HttpURLConnection.HTTP_OK)
            {
                val reader = connection.inputStream.bufferedReader()
                val responseJson = JSONObject(reader.readText())
                val productsJson = responseJson.getJSONArray("Products")

                var i = 0
                while (i < productsJson.length())
                {
                    val productJson = productsJson.getJSONObject(i)
                    val productdata = Product(
                        productJson.getInt("productid"),
                        productJson.getString("ProductName"),
                        productJson.getInt("ProductPrice"),
                        productJson.getInt("ProductQuantity"),
                        productJson.getString("ProductColour"),
                        productJson.getString("ProductSize"),
                        productJson.getString("image"),
                        productJson.getString("image2"),
                        productJson.getString("image3"),
                        productJson.getString("CategoryName"),
                        )
                    productList.add(productdata)

                    i++
                }
            }

            return productList.toTypedArray()
        }
        internal fun downloadImage(context: Context, product: Product)
        {
            val url = URL("http://192.168.134.37:8084/UrbanClosetApache/images/${product.imagePath}")
            val connection = (url.openConnection() as HttpURLConnection).apply {
                requestMethod = "GET"
                doInput = true
            }

            try
            {
                val cacheDirPath = context.externalCacheDir!!.absolutePath
                val imageDirPath = "${cacheDirPath}/images"

                val imageDir = File(imageDirPath)
                if (!imageDir.exists())
                    imageDir.mkdir()

                val imageSavePath = FileOutputStream("${cacheDirPath}${product.imagePath}")

                connection.connect()
                if (connection.responseCode == HttpURLConnection.HTTP_OK)
                {
                    val bitmap = BitmapFactory.decodeStream(connection.inputStream)
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, imageSavePath)
                }
            }
            catch (ex: Exception)
            {
                Log.e("downloadImage", ex.message!!)
            }
            finally
            {
                connection.disconnect()
            }
        }
    }
    }

