package com.example.loginscreen.api

import com.example.loginscreen.models.Checkout
import com.example.loginscreen.models.Profile
import org.json.JSONArray
import org.json.JSONObject
import java.net.HttpURLConnection
import java.net.URL

class ProfileApi {

    companion object {

        internal fun getAll(id:Int): Array<Profile> {
            val profile = arrayListOf<Profile>()

            val url = URL("${Productapi.API_URL}/getproduct?catid=$id")
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
    }
}