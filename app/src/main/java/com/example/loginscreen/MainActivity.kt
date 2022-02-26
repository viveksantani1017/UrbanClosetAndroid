package com.example.loginscreen

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.*
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.example.loginscreen.adapters.CategoryGridAdapter
import com.example.loginscreen.adapters.ProductGridAdapter
import com.example.loginscreen.api.Categoryapi
import com.example.loginscreen.api.NewProductApi
import com.example.loginscreen.api.Productapi
import com.example.loginscreen.models.Category
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.navigation.NavigationView
import com.synnapps.carouselview.CarouselView
import com.synnapps.carouselview.ImageListener
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

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

        val pref = getSharedPreferences("UrbanCloset", MODE_PRIVATE)
        val UserId = pref.getInt("UserID",0)
        drawer = findViewById(R.id.drawer)
        drawerToggle = ActionBarDrawerToggle(this, drawer, R.string.open, R.string.close)

        drawer.addDrawerListener(drawerToggle)
        drawerToggle.syncState()


        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        navigationView = findViewById(R.id.navView)
        navigationView.setNavigationItemSelectedListener { menuItem ->

            when (menuItem.itemId) {
                R.id.menuprofile -> {
                    val intent = Intent(this, ProfileActivity::class.java)
                    startActivity(intent)
                    drawer.closeDrawer(GravityCompat.START)
                }
                R.id.menuorder -> {
                    if(UserId == 0)
                    {
                        MaterialAlertDialogBuilder(this)
                            .setTitle("Log In To See Your Orders")
                            .setNeutralButton("cancel") { dialog, which ->
                                closeContextMenu()
                            }
                            .setPositiveButton("Login") { dialog, which ->
                                val intent = Intent(this,login::class.java)
                                startActivity(intent)
                            }
                            .show()
                    }
                    else
                    {
                        val intent = Intent(this, OrderActivity::class.java)
                        startActivity(intent)
                        drawer.closeDrawer(GravityCompat.START)

                    }
                }
            }
            true
        }
        val horizontalScrollView = findViewById<HorizontalScrollView>(R.id.horizontalScrollView)
//        val grdProductNewest = horizontalScrollView.findViewById<GridView>(R.id.grdProductsnewest)
        val grdProducts = findViewById<GridView>(R.id.grdProducts)
        val grdProductswomen = findViewById<GridView>(R.id.grdProductswomen)

        val carouselView = findViewById(R.id.carouselView) as CarouselView
        carouselView.setPageCount(sampleImages.size)
        carouselView.setImageListener(imageListener)

        CoroutineScope(Dispatchers.IO).launch {
            grdProducts.setOnItemClickListener { _, view, _, _ ->
                val catId = view.contentDescription.toString().toInt()
                val intent = Intent(this@MainActivity, ProductListActivity::class.java)
                intent.putExtra("CatId", catId)
                intent.putExtra("CategoryName", "Men Products")
                CoroutineScope(Dispatchers.Main).launch {
                    startActivity(intent)
                }
            }
            grdProductswomen.setOnItemClickListener { _, view, _, _ ->
                val catId = view.contentDescription.toString().toInt()
                val intent = Intent(this@MainActivity, ProductListActivity::class.java)
                intent.putExtra("CatId", catId)
                intent.putExtra("CategoryName", "Women Products")
                CoroutineScope(Dispatchers.Main).launch {
                    startActivity(intent)
                }
            }
            val categorymen = Categoryapi.getMens()
            val categorywomen = Categoryapi.getWomens()
            val newproduct = NewProductApi.getAll()
            if (categorymen.isNotEmpty() && categorywomen.isNotEmpty()) {
                for (category in categorymen)
                    Categoryapi.downloadImage(this@MainActivity, category)
                for (category in categorywomen)
                    Categoryapi.downloadImage(this@MainActivity, category)
//                for (product in newproduct)
//                    NewProductApi.downloadImage(this@MainActivity,product)

                val adaptermen = CategoryGridAdapter(this@MainActivity, categorymen)
                withContext(Dispatchers.Main) { grdProducts.adapter = adaptermen }
                val adapterwomen = CategoryGridAdapter(this@MainActivity, categorywomen)
                withContext(Dispatchers.Main) { grdProductswomen.adapter = adapterwomen }
//                var adapternewproduct = ProductGridAdapter(this@MainActivity,newproduct)
//                withContext(Dispatchers.Main){grdProductNewest.adapter = adapternewproduct}
            } else {
                withContext(Dispatchers.Main) {
                    Toast.makeText(this@MainActivity, "Empty Category Array", Toast.LENGTH_SHORT)
                        .show()
                }
            }
        }

    }

    var imageListener: ImageListener = object : ImageListener {
        override fun setImageForPosition(position: Int, imageView: ImageView) {
            imageView.setImageResource(sampleImages[position])
        }


    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            if (drawer.isDrawerOpen(GravityCompat.START))
                drawer.closeDrawer(GravityCompat.START)
            else
                drawer.openDrawer(GravityCompat.START)
        }

        return super.onOptionsItemSelected(item)
    }

}