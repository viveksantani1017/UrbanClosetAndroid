package com.example.loginscreen

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.google.android.material.textfield.TextInputEditText
import kotlinx.coroutines.*
import org.json.JSONArray
import org.json.JSONObject
import java.lang.Exception
import java.net.HttpURLConnection
import java.net.URL

class login : AppCompatActivity() {
    private lateinit var etusername: TextInputEditText
    private lateinit var etpassword: TextInputEditText
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        supportActionBar?.hide()
        val pref = getSharedPreferences("UrbanCloset", MODE_PRIVATE)
        val userId = pref.getInt("UserID", 0)
        if (userId != 0) {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
        val tvsignup = findViewById<TextView>(R.id.tvlogin)
        tvsignup.setOnClickListener {
            var intent = Intent(this, SignUp::class.java)
            startActivity(intent)

        }
        etusername = findViewById(R.id.edittextusername)
        etpassword = findViewById(R.id.edittextpassword)
        var btnlogin = findViewById<Button>(R.id.btnsignup)
        btnlogin.setOnClickListener {
            CoroutineScope(Dispatchers.IO).launch {
                loginButtonClicked()
            }
        }

    }

    private fun loginButtonClicked() {
        val username = etusername.text.toString()
        val password = etpassword.text.toString()
        if (username.isEmpty() || password.isEmpty())
            return
        CoroutineScope(Dispatchers.IO).launch {

            var userid = authenticate(username, password)
            if ( userid > 0) {

                val pref = getSharedPreferences("UrbanCloset", MODE_PRIVATE)
                val prefEditor = pref.edit()

                prefEditor.putInt("UserID", userid)
                prefEditor.apply()

                withContext(Dispatchers.Main) {
                    val intent = Intent(this@login, MainActivity::class.java)
                    startActivity(intent)
                    finish()
                }

            } else {
                withContext(Dispatchers.Main) {
                    Toast.makeText(this@login, "Wrong Info", Toast.LENGTH_SHORT).show()
                }
            }

        }
    }

    private fun authenticate(username: String, password: String): Int {
        val url = URL("http://192.168.102.37:8084/UrbanClosetApache/users")
        val connection = (url.openConnection() as HttpURLConnection).apply {
            requestMethod = "POST"
            doInput = true
            doOutput = true
            setRequestProperty("Content-Type", "Application/json")
            setChunkedStreamingMode(0)
        }
        try {
//            Creating json object to put data
            val jsonObject = JSONObject()
            jsonObject.put("name", username)
            jsonObject.put("pass", password)
//            writing data to url
            val writer = connection.outputStream.bufferedWriter()
            writer.write(jsonObject.toString())
            writer.flush()
//            Checking if url is read and response is sent or not
            if (connection.responseCode == HttpURLConnection.HTTP_OK) {
//                reading the response data
                val reader = connection.inputStream.bufferedReader()
                val jsonResponseString = reader.readText()
                val responseJson = JSONObject(jsonResponseString)
                return responseJson.getInt("value")
            }
        } catch (Ex: Exception) {
            Log.i("Error", Ex.message.toString())
        } finally {
            connection.disconnect()
        }
        return 0
    }
    private fun fetchData(): String? {
        val url = URL("http://192.168.102.37:8084/UrbanClosetApache/users")
        val connection = (url.openConnection() as HttpURLConnection).apply {
            requestMethod = "GET"
            doOutput = true
            setRequestProperty("Content-Type", "Application/json")
            setChunkedStreamingMode(0)
        }
        try {

//                reading the response data
            if(connection.responseCode == HttpURLConnection.HTTP_OK) {
                val reader = connection.inputStream.bufferedReader()
                val jsonResponseString = reader.readText()
                val responseJson = JSONArray(jsonResponseString)
                return responseJson.getString(1)
            }
            else
            {
                Log.i("Error","Contact Meet Sir")
            }
        }
        catch (ex:Exception)
        {
            Log.i("Fetch data error",ex.toString())
        }
        finally {
            connection.disconnect()
        }
        return null
    }
}
