package com.example.loginscreen.models

data class Orderdetails(
    var id: Int,
    var orderedproductid: Int,
    var productimage: String,
    var productName: String,
    var size: String,
    var quantity: Int,
    var totalPrice: Int
)