package com.example.justtalk.Kotlin

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.justtalk.Kotlin.models.User
import com.example.justtalk.R
import com.example.justtalk.databinding.ModelChatBinding
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class ChatListAdapter(private val mListener:ChatClickCallback) : RecyclerView.Adapter<ChatListAdapter.ChatViewHolder>() {
    private var oldList:List<String> = emptyList()

    class ChatViewHolder(private val view: View,private val context: Context,private val mListener: ChatClickCallback):RecyclerView.ViewHolder(view){
        lateinit private var reference:DatabaseReference
        lateinit private var mBinding : ModelChatBinding
        init{
            reference = Firebase.database.reference.child("User")
            mBinding = DataBindingUtil.bind(view)!!
        }
        fun bind(id:String){
               val user: User
               Glide.with(context).load(user.dp).into(mBinding.dpUser)
               mBinding.nameUser.setText(user.name)
               mBinding.card.setOnClickListener{
                   mListener.onClick(id,view)
               }
        }
        companion object{
            fun getInstance(parent:ViewGroup,mListener: ChatClickCallback): ChatViewHolder {
                val view = LayoutInflater.from(parent.context).inflate(R.layout.model_chat,parent,false)
                return ChatViewHolder(view,parent.context,mListener)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatViewHolder {
        return ChatViewHolder.getInstance(parent,mListener);
    }

    override fun onBindViewHolder(holder: ChatViewHolder, position: Int) {
        holder.bind(oldList.get(position))
    }

    override fun getItemCount() = oldList.size

    public fun submitList(newList:List<String>){
        val diffutil = DiffUtilChat(oldList,newList)
        val calcdiff = DiffUtil.calculateDiff(diffutil)
        oldList = newList
        calcdiff.dispatchUpdatesTo(this)
    }
}