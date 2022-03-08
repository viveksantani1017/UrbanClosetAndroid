package com.example.loginscreen

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.TextView
import com.example.loginscreen.api.ProfileApi
import com.google.android.material.button.MaterialButton
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ProfileActivity : AppCompatActivity() {
    private lateinit var name: TextView
    private lateinit var email: TextView
    private lateinit var phone: TextView
    private lateinit var address: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.title = "Your Profile"
        name = findViewById(R.id.name)
        email = findViewById(R.id.email)
        phone = findViewById(R.id.phone)
        address = findViewById(R.id.address)

        val userid = getSharedPreferences("UrbanCloset", MODE_PRIVATE).getInt("UserID", 0)
        CoroutineScope(Dispatchers.IO).launch {
            ProfileApi.getAll(userid).also {
                withContext(Dispatchers.Main) {
                    name.text = it.UserName
                    email.text = it.Useremail
                    phone.text = it.UserPhNo
                    address.text = it.UserAddress
                }

            }
        }
        findViewById<MaterialButton>(R.id.btnedit).setOnClickListener {
            startActivity(Intent(this,EditProfileAcitvity::class.java))
        }

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