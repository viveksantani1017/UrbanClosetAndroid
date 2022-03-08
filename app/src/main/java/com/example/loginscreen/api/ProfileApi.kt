package com.example.loginscreen.api

import com.example.loginscreen.models.Checkout
import com.example.loginscreen.models.Profile
import org.json.JSONArray
import org.json.JSONObject
import java.net.HttpURLConnection
import java.net.URL

class ProfileApi {

    companion object {

        internal fun getAll(id:Int): Profile {
            val profile = arrayListOf<Profile>()
            val url = URL("${Productapi.API_URL}/getuser?userid=$id")
            val connection = (url.openConnection() as HttpURLConnection).apply {
                requestMethod = "GET"
                doInput = true
                setRequestProperty("Content-Type", "application/json")
                setChunkedStreamingMode(0)
            }
            if (connection.responseCode == HttpURLConnection.HTTP_OK) {
                val reader = connection.inputStream.bufferedReader()
                val responseJson = JSONObject(reader.readText())
                val profileJson = responseJson.getJSONObject("User")

                    return Profile(
                        profileJson.getInt("Userid"),
                        profileJson.getString("UserName"),
                        profileJson.getString("UserEmail"),
                        profileJson.getString("Address1"),
                        profileJson.getString("UserPhNo"),
                        profileJson.getString("Address1")
                    )
            }

            return null!!
        }
    }
}