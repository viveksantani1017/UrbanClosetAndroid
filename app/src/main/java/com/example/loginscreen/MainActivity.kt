package com.example.loginscreen

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.Button
import android.widget.ImageView
import android.widget.ScrollView
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationView
import com.synnapps.carouselview.CarouselView
import com.synnapps.carouselview.ImageListener

class MainActivity : AppCompatActivity() {
    var sampleImages = intArrayOf(
        R.drawable.menbanner,
        R.drawable.womenbanner,
    )
    private lateinit var drawer: DrawerLayout
    private lateinit var drawerToggle: ActionBarDrawerToggle
    private lateinit var navigationView: NavigationView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        drawer = findViewById(R.id.drawer)
        drawerToggle = ActionBarDrawerToggle(this, drawer, R.string.open, R.string.close)

        drawer.addDrawerListener(drawerToggle)
        drawerToggle.syncState()


        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        navigationView = findViewById(R.id.navView)


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
    override fun onOptionsItemSelected(item: MenuItem): Boolean
    {
        if (item.itemId == android.R.id.home)
        {
            if (drawer.isDrawerOpen(GravityCompat.START))
                drawer.closeDrawer(GravityCompat.START)
            else
                drawer.openDrawer(GravityCompat.START)
        }

        return super.onOptionsItemSelected(item)
    }
}