package com.example.loginscreen

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView

class login : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        supportActionBar?.hide()
        val btnlogin = findViewById<Button>(R.id.btnlogin)
        btnlogin.setOnClickListener {
            val intent = Intent(this, SignUp::class.java).apply {
                startActivity(intent)
            }
        }
    }
}