package com.example.justtalk.Kotlin.models

import java.io.Serializable

data class User(
    var id:String?="", //Ref Id
    var uid:String?="",
    var name:String?="",
    var email:String?="",
    var dp:String?="",
    var bday:String?="",
    var bio:String?="",
    var phno:String?="",
    var chatroomref:String?=""
):Serializable{

}

data class Parcel(
    var userCurrent:User,
    var ssid:String?
):Serializable{

}
data class RequestRoom(
    var id:String?="",
    var userIds:List<String?> = ArrayList()
)

data class FriendsRoom(
    var id:String?="",
    var userIds:List<String?> = ArrayList()
)

data class ChatRoom(
    var id:String?="",
    var userIds:List<String?> = ArrayList()
)

data class UserToken(
    var id:String,
    var user:User
)








