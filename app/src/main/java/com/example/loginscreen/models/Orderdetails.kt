package com.example.loginscreen.models

data class Orderdetails(
    var id: Int,
    var productimage: String,
    var productName: String,
    var size: String,
    var quantity: String,
    var address: String,
    var totalPrice: Int
)