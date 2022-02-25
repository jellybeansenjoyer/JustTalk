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
    var chatrefs:List<String>?=null, //No of views in chat fragment
    var friendrefs:List<String>?=null, //No of friends that can be texted
    var reqrefs:List<String>?=null //No of Reqests in the Activity
):Serializable{

}

