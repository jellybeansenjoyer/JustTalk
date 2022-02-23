package com.example.justtalk.Kotlin.Adapter

import android.view.View
import com.example.justtalk.Kotlin.models.User

interface ChatClickCallback {
    fun onClick(user:User,view: View)
}