package com.example.justtalk.Kotlin.models

data class Message(
    var id:String,
    var type:String,
    var content:Any,
    var sender:String,
    var reciever:String,
    var timestamp:String
)
