package com.example.loginscreen

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.GridView
import android.widget.TextView
import androidx.cardview.widget.CardView
import com.example.loginscreen.adapters.cartGridAdapter
import com.example.loginscreen.api.CartApi
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class CartActivity : AppCompatActivity() {
    lateinit var btncheckout:Button
    lateinit var btnlogin:Button
    lateinit var errorInfo:TextView
    lateinit var totalCard:CardView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cart)

        val cartlist = findViewById<GridView>(R.id.orderlist)
        totalCard = findViewById(R.id.totalcard)
        btncheckout = findViewById(R.id.btncheckout)
        btnlogin = findViewById(R.id.btnlogin)
        errorInfo = findViewById(R.id.ErrorInfo)
        supportActionBar!!.setTitle("Cart")
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val userid = getSharedPreferences("UrbanCloset", MODE_PRIVATE).getInt("UserID",0)
        if(userid == 0)
        {
            btncheckout.visibility = View.GONE
            totalCard.visibility = View.GONE
            errorInfo.text = getString(R.string.loginerrorcart)
            btnlogin.visibility = View.VISIBLE
            btnlogin.text = getString(R.string.login)
            btnlogin.setOnClickListener {
                startActivity(Intent(this,login::class.java))
                recreate()
            }
        }

        CoroutineScope(Dispatchers.IO).launch {
            val cartproducts = CartApi.getAll(this@CartActivity)

            if (cartproducts.isNotEmpty()) {
                val carttotal = CartApi.getTotalPrice(this@CartActivity)
                val adapter = cartGridAdapter(this@CartActivity, cartproducts)
                withContext(Dispatchers.Main) {
                    cartlist.adapter = adapter
                    findViewById<TextView>(R.id.totalpricevalue).text = "₹${carttotal[0]}"
                }
            } else {
                withContext(Dispatchers.Main) {
                    btncheckout.visibility = View.GONE
                    totalCard.visibility = View.GONE
                    errorInfo.text = getString(R.string.emptycart)
                }
            }
        }
        findViewById<Button>(R.id.btncheckout).setOnClickListener {
                val intent = Intent(this,checkout_Activity::class.java)
                startActivity(intent)
        }
    }



    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}