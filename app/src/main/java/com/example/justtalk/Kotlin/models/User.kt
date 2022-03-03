package com.example.justtalk.Kotlin.models

import java.io.Serializable

data class User(
    var id:String?="", //Ref Id
    var name:String?="",
    var email:String?="",
    var dp:String?="",
    var bday:String?="",
    var bio:String?="",
    var phno:String?="",
    var chatrefs:ArrayList<String>?= ArrayList(), //No of views in chat fragment
    var friendrefs:ArrayList<String>?= ArrayList(), //No of friends that can be texted
    var reqrefs:ArrayList<String>?=ArrayList() //No of Reqests in the Activity
):Serializable{

}

