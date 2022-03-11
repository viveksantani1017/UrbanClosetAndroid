package com.example.loginscreen

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.example.loginscreen.api.Productapi
import com.google.android.material.textfield.TextInputEditText
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.lang.Exception
import java.net.HttpURLConnection
import java.net.URL

class SignUp : AppCompatActivity() {
    private lateinit var etusername: TextInputEditText
    private lateinit var etemail: TextInputEditText
    private lateinit var etphonenumber: TextInputEditText
    private lateinit var etpassword: TextInputEditText


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)
        supportActionBar?.hide()
        val tvlogin = findViewById<TextView>(R.id.tvsignup)
        tvlogin.setOnClickListener {
            val intent = Intent(this, login::class.java)
            startActivity(intent)
        }

        val pref = getSharedPreferences("UrbanCloset", MODE_PRIVATE)
        val userId = pref.getInt("UserdId", 0)
        if (userId != 0) {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
        etusername = findViewById(R.id.edittextusername)
        etpassword = findViewById(R.id.edittextpassword)
        etphonenumber = findViewById(R.id.edittextphonenumber)
        etemail = findViewById(R.id.edittextemail)

        val btnsignup = findViewById<Button>(R.id.btnsignup)
        btnsignup.setOnClickListener {
            CoroutineScope(Dispatchers.IO).launch {
                signupButtonClicked()
            }
        }

    }

    private fun signupButtonClicked() {
        val username = etusername.text.toString()
        val password = etpassword.text.toString()
        val phonenumber = etphonenumber.text.toString()
        val email = etemail.text.toString()

            if (username.isEmpty() || password.isEmpty() || email.isEmpty() || phonenumber.isEmpty())
                return
            CoroutineScope(Dispatchers.IO).launch {
                if (authenticate(username, password, phonenumber, email)) {

                withContext(Dispatchers.Main) {
                        val intent = Intent(this@SignUp, login::class.java)
                        startActivity(intent)
                        finish()
                    }

                } else {
                    withContext(Dispatchers.Main) {
                        Toast.makeText(this@SignUp, "Wrong Info", Toast.LENGTH_SHORT).show()
                    }
                }

            }
        }
    private fun authenticate(username: String, password: String,phonenumber: String,email: String): Boolean {
        val url = URL("${Productapi.API_URL}/signup")
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
            jsonObject.put("phno", phonenumber)
            jsonObject.put("email", email)

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
