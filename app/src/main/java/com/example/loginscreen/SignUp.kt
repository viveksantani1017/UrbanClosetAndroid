package com.example.loginscreen

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView

class SignUp : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)
        supportActionBar?.hide()
        val tvlogin = findViewById<TextView>(R.id.tvsignup)
        tvlogin.setOnClickListener {
            var intent = Intent(this, login::class.java)
            startActivity(intent)
        }
    }
}