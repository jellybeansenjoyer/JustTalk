package com.example.justtalk.Kotlin.UI

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.justtalk.Kotlin.Adapter.FriendListAddFriendListener
import com.example.justtalk.Kotlin.models.User
import com.example.justtalk.R
import com.example.justtalk.databinding.FriendViewBinding

private const val TAG = "FriendsListAdapter"
class FriendsListAdapter(private val requestListener:FriendListAddFriendListener) : RecyclerView.Adapter<FriendsListAdapter.FriendsViewHolder>() {
    private var oldList:List<User> = emptyList()
    class FriendsViewHolder(private val view: View,private val parent:ViewGroup,private val requestListener: FriendListAddFriendListener) : RecyclerView.ViewHolder(view){
        private var mBinding: FriendViewBinding

        init {
            mBinding = DataBindingUtil.bind(view)!!
        }
        fun bind(user:User){
            mBinding.nameUser.setText(user.name)
            Log.e(TAG,user.dp.toString())
            Glide.with(parent.context).load(user.dp).into(mBinding.userPhoto)
            mBinding.taptobefriend.setOnClickListener{
                Log.e(TAG,user.dp.toString())
                requestListener.sendRequest(user)
            }
        }

        companion object{
            public fun getInstance(parent:ViewGroup,requestListener: FriendListAddFriendListener):FriendsViewHolder{
                val view = LayoutInflater.from(parent.context).inflate(R.layout.friend_view,parent,false)
                return FriendsViewHolder(view,parent,requestListener)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FriendsViewHolder {
        return FriendsViewHolder.getInstance(parent,requestListener)
    }

    override fun onBindViewHolder(holder: FriendsViewHolder, position: Int) {
        holder.bind(oldList.get(position))
    }

    override fun getItemCount() = oldList.size

    fun submitList(newList:List<User>){
        val cb= object : DiffUtil.Callback(){
            override fun getOldListSize() = oldList.size

            override fun getNewListSize() = newList.size

            override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
                return oldList[oldItemPosition]==newList[newItemPosition]
            }

            override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
                return oldList[oldItemPosition] == newList[newItemPosition]
            }
        }
        val calc = DiffUtil.calculateDiff(cb)
        oldList = newList
        calc.dispatchUpdatesTo(this)
        }
}