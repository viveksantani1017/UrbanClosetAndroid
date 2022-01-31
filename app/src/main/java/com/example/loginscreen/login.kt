package com.example.loginscreen

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import kotlinx.coroutines.*
import org.json.JSONObject
import java.lang.Exception
import java.net.HttpURLConnection
import java.net.URL
import kotlin.coroutines.coroutineContext

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
        val tvsignup = findViewById<TextView>(R.id.tvsignup)
        tvsignup.setOnClickListener {
            var intent = Intent(this, SignUp::class.java)
            startActivity(intent)

        }
        etusername = findViewById(R.id.edittextusername)
        etpassword = findViewById(R.id.edittextpassword)
        var btnlogin = findViewById<Button>(R.id.btnlogin)
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
            if (authenticate(username, password)) {

                val jsonObject = JSONObject()
                Log.i("Uerid",jsonObject.getString("value"))
                val pref = getSharedPreferences("UrbanCloset", MODE_PRIVATE)
                val prefEditor = pref.edit()
                prefEditor.putString("UserID", jsonObject.getString("value"))
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

    private fun authenticate(username: String, password: String): Boolean {
        val url = URL("http://192.168.240.37:8084/UrbanClosetApache/users")
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
                return responseJson.getBoolean("status")
            }
        } catch (Ex: Exception) {
            Log.i("Error", Ex.message.toString())
        } finally {
            connection.disconnect()
        }
        return false
    }
}
