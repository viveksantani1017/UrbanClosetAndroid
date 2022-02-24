package com.example.loginscreen.models

data class Product(var id: Int, var name: String, var price: Int,var description:String,var quantity: Int,var color: String,var size: String, var images: Array<String>,var categoryname: String, var inwishlist:Boolean)