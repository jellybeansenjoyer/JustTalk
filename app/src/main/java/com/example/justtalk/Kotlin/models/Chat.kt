package com.example.justtalk.Kotlin.models

data class Chat( //Room
    var id:String, //roomId
    var sender:String,
    var reciever:String,
    var messages:List<Message>
)
