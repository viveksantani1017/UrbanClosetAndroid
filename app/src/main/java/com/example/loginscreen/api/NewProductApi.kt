package com.example.loginscreen.api

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import com.example.loginscreen.models.NewProductModel
import com.example.loginscreen.models.Product
import org.json.JSONObject
import java.io.File
import java.io.FileOutputStream
import java.lang.Exception
import java.net.HttpURLConnection
import java.net.URL

class NewProductApi {
    companion object {

        internal fun getAll(): Array<NewProductModel> {
            val productList = arrayListOf<NewProductModel>()

            val url = URL("${Productapi.API_URL}/newproduct")
            val connection = (url.openConnection() as HttpURLConnection).apply {
                requestMethod = "GET"
                doInput = true
                setRequestProperty("Content-Type", "application/json")
                setChunkedStreamingMode(0)
            }
            if (connection.responseCode == HttpURLConnection.HTTP_OK) {
                val reader = connection.inputStream.bufferedReader()
                val responseJson = JSONObject(reader.readText())
                val productsJson = responseJson.getJSONArray("newproducts")

                var i = 0
                while (i < productsJson.length()) {

                    val productJson = productsJson.getJSONObject(i)
                    val productdata = NewProductModel(
                        productJson.getInt("productid"),
                        productJson.getString("ProductName"),
                        productJson.getString("CategoryName"),
                        productJson.getInt("ProductPrice"),
                        productJson.getString("image")
                    )
                    productList.add(productdata)

                    i++
                }
            }

            return productList.toTypedArray()
        }

        internal fun downloadImage(context: Context, product: NewProductModel) {
            val url = URL("${Productapi.API_URL}/images/${product.image}")
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

                val imageSavePath = FileOutputStream("${imageDirPath}${product.image}")
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