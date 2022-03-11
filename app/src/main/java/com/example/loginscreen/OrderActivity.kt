package com.example.loginscreen

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import com.example.loginscreen.adapters.OrderListAdapter
import com.example.loginscreen.api.OrderApi
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.lang.Exception

class OrderActivity : AppCompatActivity() {
    private lateinit var tverrorinfo:TextView
    private lateinit var btnlogin:Button
    private lateinit var tvorderhistory:TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_order)
        supportActionBar?.title = "My orders"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        tverrorinfo = findViewById(R.id.tvordererrorinfo)
        btnlogin = findViewById(R.id.btnorderogin)
        tvorderhistory = findViewById(R.id.tvorderdetails)
        val userid = getSharedPreferences("UrbanCloset", MODE_PRIVATE).getInt("UserID",0)
        val orderlist = findViewById<GridView>(R.id.orderlist)
        if(userid == 0)
        {
           try {
                tverrorinfo.text = getString(R.string.loginerrorcart)
                btnlogin.visibility = View.VISIBLE
                btnlogin.setOnClickListener {
                    startActivity(Intent(this,login::class.java))
//                    recreate()
                }

           }
           catch (ex: Exception)
           {
               Log.e("OrderError",ex.message.toString())
           }
        }
        CoroutineScope(Dispatchers.IO).launch {
            orderlist.setOnItemClickListener { _, view, _, _ ->
                val orderId = view.contentDescription.toString().toInt()
                val intent = Intent(this@OrderActivity, orderDetailActivity::class.java)
                intent.putExtra("OrderId", orderId)
                CoroutineScope(Dispatchers.Main).launch {
                    startActivity(intent)
                }
            }
            val orders = OrderApi.getAll(this@OrderActivity)
            if (orders.isNotEmpty()) {
                val adapter = OrderListAdapter(this@OrderActivity, orders)
                withContext(Dispatchers.Main) { orderlist.adapter = adapter }
            } else {
                withContext(Dispatchers.Main) {
                   tverrorinfo.text = getString(R.string.emptyorder)
                }
            }
        }

    }
    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}