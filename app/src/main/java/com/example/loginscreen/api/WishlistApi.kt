package com.example.loginscreen.api

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import com.example.loginscreen.models.wishlistmodel
import org.json.JSONObject
import java.io.File
import java.io.FileOutputStream
import java.lang.Exception
import java.net.HttpURLConnection
import java.net.URL

class WishlistApi {

    companion object {

        internal fun getAll(id:Int): Array<wishlistmodel> {
            val wishlist = arrayListOf<wishlistmodel>()

            val url = URL("${Productapi.API_URL}/getwishlist?userid=${id}")
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
                    var wishlistdata = wishlistmodel(
                        orderJson.getInt("Productid"),
                        orderJson.getString("image"),
                        orderJson.getString("ProductName"),
                        orderJson.getString("CategoryName"),
                        orderJson.getInt("ProductQuantity"),
                        orderJson.getString("ProductPrice")
                    )
                    wishlist.add(wishlistdata)
                    i++
                }
            } else {
                Log.i("error", "Hello")
            }

            return wishlist.toTypedArray()
        }

        internal fun downloadImage(context: Context, product: wishlistmodel) {
            val url = URL("${Productapi.API_URL}/images/${product.productimage}")
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
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 80, imageSavePath)
                }
            } catch (ex: Exception) {
                Log.e("downloadImage", ex.message!!)
            } finally {
                connection.disconnect()
            }
        }
        internal fun deletewishlist(
            userid: Int,
            productid: Int
        ): JSONObject {
            val url =
                URL("${Productapi.API_URL}/removewishlist?userid=${userid}&productid=${productid}")
            val connection = (url.openConnection() as HttpURLConnection).apply {
                requestMethod = "GET"
                doInput = true
                setRequestProperty("Content-Type", "Application/json")
                setChunkedStreamingMode(0)
            }

            try {
                if (connection.responseCode == HttpURLConnection.HTTP_OK) {
                    val reader = connection.inputStream.bufferedReader()
                    val jsonResponseString = reader.readText()
                    return JSONObject(jsonResponseString)
                }
            } catch (Ex: Exception) {
                Log.i("Remove From Wishlist Error", Ex.message.toString())
            } finally {
                connection.disconnect()
            }
            return JSONObject()
        }


    }


}