package com.example.justtalk.Kotlin.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.justtalk.Kotlin.models.Message
import com.example.justtalk.R
import com.example.justtalk.databinding.ModelMessageBinding

class MessageThreadAdapter : RecyclerView.Adapter<MessageThreadAdapter.MessageViewHolder>() {
    private var oldList:List<Message> = emptyList()

    class MessageViewHolder(private val view: View):RecyclerView.ViewHolder(view){
        lateinit private var mBinding:ModelMessageBinding
        init{
            mBinding = DataBindingUtil.bind(view)!!
        }
        fun bind(message:Message){
            mBinding.message.setText(message.content)
        }
        companion object{
            @JvmStatic
            fun getInstance(parent:ViewGroup): MessageViewHolder {
                val view = LayoutInflater.from(parent.context).inflate(R.layout.model_message,parent,false)
                return MessageViewHolder(view)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageViewHolder {
        return MessageViewHolder.getInstance(parent)
    }

    override fun onBindViewHolder(holder: MessageViewHolder, position: Int) {
            holder.bind(oldList.get(position))
    }

    override fun getItemCount() = oldList.size

    fun submitList(newList:List<Message>){
        val diffUtilCb = object : DiffUtil.Callback(){
            override fun getOldListSize() = oldList.size
            override fun getNewListSize() = newList.size
            override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
                return oldList.get(oldItemPosition)==newList.get(newItemPosition)
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