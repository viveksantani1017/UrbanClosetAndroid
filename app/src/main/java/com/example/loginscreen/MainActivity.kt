package com.example.loginscreen

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
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