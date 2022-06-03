package com.example.justtalk.Kotlin.Adapter

import androidx.recyclerview.widget.DiffUtil
import com.example.justtalk.Kotlin.models.ChatFrag
import com.example.justtalk.Kotlin.models.User

class DiffUtilChat(private val oldList:List<ChatFrag>, private val newList:List<ChatFrag>) : DiffUtil.Callback() {
    override fun getOldListSize() = oldList.size
    override fun getNewListSize() = newList.size
    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList.get(oldItemPosition).equals(newList.get(newItemPosition))
    }
    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList.get(oldItemPosition).equals(newList.get(newItemPosition))
    }
}