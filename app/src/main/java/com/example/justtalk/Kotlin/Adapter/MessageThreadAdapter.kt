package com.example.justtalk.Kotlin.Adapter

import android.content.Context
import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCompositionContext
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.justtalk.Kotlin.models.Message
import com.example.justtalk.Kotlin.models.User
import com.example.justtalk.R
import com.example.justtalk.databinding.EndUserMessageModelBinding
import com.example.justtalk.databinding.ModelMessageBinding
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.ktx.storage

private const val TAG = "MessageThreadAdapter"
class MessageThreadAdapter(private var mEndUser: User,private var mUser:User) : RecyclerView.Adapter<MessageThreadAdapter.MessageViewHolder>() {
    private var oldList: List<Message> = emptyList()
    class MessageViewHolder(private val view: View,private val flag:Int,private var mEndUser: User,private val mContext:Context,var mUser:User) : RecyclerView.ViewHolder(view) {
        lateinit private var mBinding: ViewDataBinding
        lateinit private var mStorage : StorageReference
        lateinit private var mImage: Uri
        init {
            mStorage = Firebase.storage.reference.child("UserDP")
            when(flag){
                0 ->{
                    mBinding = DataBindingUtil.bind<ModelMessageBinding>(view)!!
                }
                1 ->{
                    mBinding = DataBindingUtil.bind<EndUserMessageModelBinding>(view)!!
                }
            }
        }

        fun bind(message: Message) {
             when(flag){
                 0->{
                     (mBinding as ModelMessageBinding).run{

                         this.message.setText(message.content)
                         mStorage.child("${mEndUser.uid}.jpg").downloadUrl.addOnCompleteListener {
                             if(it.isComplete){
                                 Glide.with(mContext.applicationContext).load(it.result).into(this.userDp)
                             }
                         }
                     }

                 }
                 1->{
                     (mBinding as EndUserMessageModelBinding).run{
                         this.message.setText(message.content)
                         mStorage.child("${mUser.uid}.jpg").downloadUrl.addOnCompleteListener {
                             if(it.isComplete){
                                 Glide.with(mContext.applicationContext).load(it.result).into(this.userDp)
                             }
                         }
                     }
                 }
             }
        }

        companion object {
            @JvmStatic
            fun getInstance(parent: ViewGroup,flag:Int,mEndUser: User,context:Context,mUser:User): MessageViewHolder {
                var view:View?=null
                when(flag){
                        1->{
                            view = LayoutInflater.from(parent.context)
                                .inflate(R.layout.end_user_message_model, parent, false)
                        }
                        0->{
                             view = LayoutInflater.from(parent.context)
                                .inflate(R.layout.model_message, parent, false)
                        }
                }
                return MessageViewHolder(view!!,flag,mEndUser,context,mUser = mUser)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageViewHolder {
        return MessageViewHolder.getInstance(parent,viewType,mEndUser,parent.context,mUser)
    }

    override fun onBindViewHolder(holder: MessageViewHolder, position: Int) {
        holder.bind(oldList.get(position))
    }

    override fun getItemViewType(position: Int): Int {
        if(oldList.get(position).from.equals(mEndUser.id))
            return 0
        else
            return 1
    }

    override fun getItemCount() = oldList.size

    fun submitList(newList: List<Message>) {
        val diffUtilCb = object : DiffUtil.Callback() {
            override fun getOldListSize() = oldList.size
            override fun getNewListSize() = newList.size
            override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
                return oldList.get(oldItemPosition) == newList.get(newItemPosition)
            }

            override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
                return oldList.get(oldItemPosition).equals(newList.get(newItemPosition))
            }

        }
        val calc = DiffUtil.calculateDiff(diffUtilCb)
        oldList = newList
        calc.dispatchUpdatesTo(this)
        this.notifyDataSetChanged()
    }

}