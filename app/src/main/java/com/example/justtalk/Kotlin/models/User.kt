package com.example.justtalk.Kotlin.models

data class User(
    var id:String,
    var name:String,
    var email:String,
    var dp:String,
    var bday:String,
    var bio:String,
    var phno:String,
    var chatrefs:List<String>,
    var friendrefs:List<String>,
    var reqrefs:List<String>
)


