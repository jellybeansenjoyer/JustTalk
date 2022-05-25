package com.example.justtalk.Kotlin.models

import java.io.Serializable

/*Serializable is a wrapper that wraps the object of the class
* can be used for transfer of data via intents cross activity */
data class User(
    var id:String?="",
    var uid:String?="",
    var name:String?="",
    var email:String?="",
    var dp:String?="",
    var bday:String?="",
    var bio:String?="",
    var phno:String?="",
    var chatroomref:String?=""
):ChatFrag()

data class Parcel(
    var userCurrent:User,
    var ssid:String?
):Serializable{

}
abstract class ChatFrag():Serializable{

}



