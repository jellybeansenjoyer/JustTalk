package com.example.justtalk.Kotlin.models

data class Message( //Message
    var id:String,  //Ref of Room
    var type:String, //content type
    var content:Any,
    var sender:String, //id of sender
    var reciever:String, //id of reciever
    var timestamp:String //timestamp
)
