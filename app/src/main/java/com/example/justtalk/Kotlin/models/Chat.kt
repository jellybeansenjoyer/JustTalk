package com.example.justtalk.Kotlin.models

data class Chat(
    var id:String,
    var sender:String,
    var reciever:String,
    var messages:List<Message>
)
