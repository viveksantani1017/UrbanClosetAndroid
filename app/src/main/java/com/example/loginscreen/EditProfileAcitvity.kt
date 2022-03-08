package com.example.loginscreen

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.example.loginscreen.api.Productapi
import com.example.loginscreen.api.ProfileApi
import com.google.android.material.button.MaterialButton
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.lang.Exception
import java.net.HttpURLConnection
import java.net.URL

class EditProfileAcitvity : AppCompatActivity() {
    private lateinit var etname: TextView
    private lateinit var etemail: TextView
    private lateinit var etphone: TextView
    private lateinit var etaddress: TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_profile_acitvity)
        supportActionBar!!.setTitle("Edit Your Profile")
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        etname = findViewById(R.id.edittextusername)
        etemail = findViewById(R.id.edittextemail)
        etphone = findViewById(R.id.edittextphonenumber)
        etaddress = findViewById(R.id.edittextaddress)

        val userid = getSharedPreferences("UrbanCloset", MODE_PRIVATE).getInt("UserID", 0)
        CoroutineScope(Dispatchers.IO).launch {
            ProfileApi.getAll(userid).also {
                withContext(Dispatchers.Main) {
                    etname.text = it.UserName
                    etemail.text = it.Useremail
                    etphone.text = it.UserPhNo
                    etaddress.text = it.UserAddress
                }

            }
        }
        findViewById<MaterialButton>(R.id.btnsubmit).setOnClickListener {
            val name = etname.text.toString()
            val email = etemail.text.toString()
            val phone = etphone.text.toString()
            val address = etaddress.text.toString()
            CoroutineScope(Dispatchers.IO).launch {
                val response = updateUser(name, email, phone, address)
                if(response.getBoolean("status"))
                {
                    withContext(Dispatchers.Main){
                        Toast.makeText(this@EditProfileAcitvity, response.getString("message"), Toast.LENGTH_SHORT).show()
                        startActivity(Intent(this@EditProfileAcitvity,ProfileActivity::class.java))
                    }
                }
                else
                {
                    withContext(Dispatchers.Main){
                        Toast.makeText(this@EditProfileAcitvity, response.getString("message"), Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    private fun updateUser(
        username: String,
        email: String,
        phone: String,
        address: String
    ): JSONObject {
        val userid = getSharedPreferences("UrbanCloset", MODE_PRIVATE).getInt("UserID", 0)
        val url = URL("${Productapi.API_URL}/getuser")
        val connection = (url.openConnection() as HttpURLConnection).apply {
            requestMethod = "PUT"
            doInput = true
            doOutput = true
            setRequestProperty("Content-Type", "Application/json")
            setChunkedStreamingMode(0)
        }
        try {
            val jsonObject = JSONObject()
            jsonObject.put("username", username)
            jsonObject.put("email", email)
            jsonObject.put("phonenumber", phone)
            jsonObject.put("address", address)
            jsonObject.put("UserId", userid)

            val writer = connection.outputStream.bufferedWriter()
            writer.write(jsonObject.toString())
            writer.flush()
            if (connection.responseCode == HttpURLConnection.HTTP_OK) {
                val reader = connection.inputStream.bufferedReader()
                val jsonResponseString = reader.readText()
                return JSONObject(jsonResponseString)
            }
        } catch (Ex: Exception) {
            Log.i("Error", Ex.message.toString())
        } finally {
            connection.disconnect()
        }
        return JSONObject()
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                finish()
                return true
            }
        }
        return super.onContextItemSelected(item)
    }
}