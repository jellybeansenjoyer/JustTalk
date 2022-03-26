package com.example.justtalk.Kotlin.Adapter

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
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.ktx.storage

class ChatListAdapter(private val mListener: ChatClickCallback) : RecyclerView.Adapter<ChatListAdapter.ChatViewHolder>() {
    private var oldList:List<User> = emptyList()
    private var mReference: DatabaseReference
    init {
        mReference = Firebase.database.reference.child("Users")
    }
    class ChatViewHolder(private val view: View,private val context: Context,private val mListener: ChatClickCallback):RecyclerView.ViewHolder(view){
        private var mBinding : ModelChatBinding
        lateinit private var mStorage: StorageReference
        init{
            mBinding = DataBindingUtil.bind(view)!!
            mStorage = Firebase.storage.reference
        }

        fun bind(user:User){
               mStorage.child("UserDP/${user.uid}.jpg").downloadUrl.addOnCompleteListener {
                   if(it.isComplete){
                       Glide.with(context.applicationContext).load(it.result).into(mBinding.dpUser)
                   }
               }
               mBinding.nameUser.setText(user.name)
               mBinding.card.setOnClickListener{
                   mListener.onClick(user,view,false)
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
        return ChatViewHolder.getInstance(parent, mListener);
    }

    override fun onBindViewHolder(holder: ChatViewHolder, position: Int) {
        holder.bind(oldList[position])
    }

    override fun getItemCount() = oldList.size

     fun submitList(newList:List<User>){
        val diffutil = DiffUtilChat(oldList,newList)
        val calcdiff = DiffUtil.calculateDiff(diffutil)
        oldList = newList
        calcdiff.dispatchUpdatesTo(this)
    }
}