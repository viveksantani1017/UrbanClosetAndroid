package com.example.loginscreen

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.loginscreen.api.Productapi
import android.net.Uri
import android.widget.TextView
import com.synnapps.carouselview.CarouselView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class DetailActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

    }
}