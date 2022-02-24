package com.example.loginscreen.api

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import com.example.loginscreen.models.Category
import com.example.loginscreen.models.Product
import org.json.JSONObject
import java.io.File
import java.io.FileOutputStream
import java.lang.Exception
import java.net.HttpURLConnection
import java.net.URL

class Categoryapi {
    companion object {

        internal fun getMens(): Array<Category> {
            val CategoryList = arrayListOf<Category>()

            val url = URL("${Productapi.API_URL}/getcategory?catid=1")
            val connection = (url.openConnection() as HttpURLConnection).apply {
                requestMethod = "GET"
                doInput = true
                setRequestProperty("Content-Type", "application/json")
                setChunkedStreamingMode(0)
            }
            if (connection.responseCode == HttpURLConnection.HTTP_OK) {
                val reader = connection.inputStream.bufferedReader()
                val responseJson = JSONObject(reader.readText())
                val CategorysJson = responseJson.getJSONArray("Man")

                var i = 0
                while (i < CategorysJson.length()) {
                    val CategoryJson = CategorysJson.getJSONObject(i)
                    val Categorydata = Category(
                        CategoryJson.getInt("id"),
                        CategoryJson.getString("CategoryName"),
                        CategoryJson.getString("catimage")
                    )
                    CategoryList.add(Categorydata)

                    i++
                }
            }

            return CategoryList.toTypedArray()
        }

        internal fun getWomens(): Array<Category> {
            val CategoryList = arrayListOf<Category>()

            val url = URL("${Productapi.API_URL}/getcategory")
            val connection = (url.openConnection() as HttpURLConnection).apply {
                requestMethod = "GET"
                doInput = true
                setRequestProperty("Content-Type", "application/json")
                setChunkedStreamingMode(0)
            }
            if (connection.responseCode == HttpURLConnection.HTTP_OK) {
                val reader = connection.inputStream.bufferedReader()
                val responseJson = JSONObject(reader.readText())
                val CategorysJson = responseJson.getJSONArray("Women")

                var i = 0
                while (i < CategorysJson.length()) {
                    val CategoryJson = CategorysJson.getJSONObject(i)
                    val Categorydata = Category(
                        CategoryJson.getInt("id"),
                        CategoryJson.getString("CategoryName"),
                        CategoryJson.getString("catimage")
                    )
                    CategoryList.add(Categorydata)

                    i++
                }
            }

            return CategoryList.toTypedArray()
        }

        internal fun downloadImage(context: Context, category: Category)
        {
            val url = URL("${Productapi.API_URL}/images/${category.imagePath}")
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

                val imageSavePath = FileOutputStream("${imageDirPath}${category.imagePath}")

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