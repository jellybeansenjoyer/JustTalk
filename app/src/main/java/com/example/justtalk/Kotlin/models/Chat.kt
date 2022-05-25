package com.example.justtalk.Kotlin.models

data class Chat( //Room
    var id:String?="", //roomId
    var sender:String?="",
    var reciever:String?="",
    var messageRoomReference:String?=""
)

data class ChatRef(
    var freindId : String?="",
    var chatRoomId : String?="" //null for groups
)
