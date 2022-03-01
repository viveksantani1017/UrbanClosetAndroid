package com.example.loginscreen.api

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import com.example.loginscreen.models.Product
import com.example.loginscreen.models.Profile
import org.json.JSONArray
import org.json.JSONObject
import java.io.File
import java.io.FileOutputStream
import java.lang.Exception
import java.net.HttpURLConnection
import java.net.URL

class ProfileApi {

    companion object {
        val API_URL = "http://192.168.1.1:8084/UrbanClosetApache"

        internal fun getAll(id:Int): Array<Profile> {
            val profile = arrayListOf<Profile>()

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
                val profileJson = responseJson.getJSONArray("Profile")

                var i = 0
                while (i < profileJson.length()) {
                    val ProfileJson = profileJson.getJSONObject(i)
                    val productdata = Profile(
                        ProfileJson.getInt("Userid"),
                        ProfileJson.getString("UserName"),
                        ProfileJson.getString("UserEmail"),
                        ProfileJson.getString("Address1"),
                        ProfileJson.getString("UserPhNo")
                    )
                    profile.add(productdata)

                    i++
                }
            }

            return profile.toTypedArray()
        }



        internal fun getProfile(id: Int): Profile? {
            val productList= arrayListOf<Profile>()
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
                    return Profile(
                        getString("name"),
                        getString("mobilenumber"),
                        getString("Email"),
                        getString("Address1")
                    )
                }
            }
            return null
        }
    }
}