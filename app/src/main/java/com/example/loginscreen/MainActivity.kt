package com.example.loginscreen

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.ScrollView
import com.synnapps.carouselview.CarouselView
import com.synnapps.carouselview.ImageListener

class MainActivity : AppCompatActivity() {
    var sampleImages = intArrayOf(
        R.drawable.menbanner,
        R.drawable.womenbanner,
    )
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

//        var scrollview = findViewById<ScrollView>(R.id.scrollView) as ScrollView
//        var btnscroll = findViewById<Button>(R.id.btnscroll)
//        btnscroll.setOnClickListener {
//            scrollview.scrollTo(0,0)
//        }
        val carouselView = findViewById(R.id.carouselView) as CarouselView;
        carouselView.setPageCount(sampleImages.size);
        carouselView.setImageListener(imageListener);

        }
        var imageListener: ImageListener = object : ImageListener {
            override fun setImageForPosition(position: Int, imageView: ImageView) {
                // You can use Glide or Picasso here
                imageView.setImageResource(sampleImages[position])
    }
}
}