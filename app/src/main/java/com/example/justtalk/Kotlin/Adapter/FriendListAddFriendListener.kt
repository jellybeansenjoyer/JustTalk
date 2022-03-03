package com.example.justtalk.Kotlin.Adapter

import com.example.justtalk.Kotlin.models.User

interface FriendListAddFriendListener {
    fun sendRequest(user: User)
}