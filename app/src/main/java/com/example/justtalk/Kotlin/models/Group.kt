package com.example.justtalk.Kotlin.models

data class Group(
    val id:String,
    val name:String,
    val members:List<User>,
    val dp:String
):ChatFrag()
