package com.example.justtalk.Kotlin

import android.view.View
import com.example.justtalk.Kotlin.models.User

interface ChatClickCallback {
    fun onClick(userId: String,view: View)
}