package com.example.justtalk.Kotlin.UI

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.justtalk.Kotlin.Adapter.ChatClickCallback
import com.example.justtalk.Kotlin.Adapter.FriendListAddFriendListener
import com.example.justtalk.Kotlin.models.User
import com.example.justtalk.R
import com.example.justtalk.databinding.FriendAcceptViewBinding
import com.example.justtalk.databinding.FriendViewBinding
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

private const val TAG = "FriendsAdapter"
class FriendsAdapter(private val currentUser:User,private val clickedUserListener:ChatClickCallback) : RecyclerView.Adapter<FriendsAdapter.FriendsListViewHolder>(),CancelTapped {
    lateinit private var mReference: DatabaseReference
    private var oldList:ArrayList<User> = ArrayList()
    init{
        mReference = Firebase.database.reference.child("RequestRoom")
    }
    class FriendsListViewHolder(private val view: View, private val parent: ViewGroup, private val cancelListener: CancelTapped,private val chatClickCallback: ChatClickCallback) : RecyclerView.ViewHolder(view){
        private var mBinding: FriendAcceptViewBinding

        init {
            mBinding = DataBindingUtil.bind(view)!!
        }

        fun bind(user: User){
            mBinding.nameUser.setText(user.name)
            Glide.with(parent.context).load(user.dp).into(mBinding.userPhoto)

            mBinding.cancelRequest.setOnClickListener{
                cancelListener.cancelledRequest(user)
            }
            mBinding.friendAccept.setOnClickListener {
                cancelListener.cancelledRequest(user)
                chatClickCallback.onClick(user,view)
            }
        }

        companion object{
            public fun getInstance(parent: ViewGroup, cancelListener: CancelTapped,chatClickCallback: ChatClickCallback):FriendsListViewHolder{
                val view = LayoutInflater.from(parent.context).inflate(R.layout.friend_accept_view,parent,false)
                return FriendsListViewHolder(view,parent,cancelListener,chatClickCallback =chatClickCallback)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FriendsListViewHolder {
        return FriendsListViewHolder.getInstance(parent,this,chatClickCallback = clickedUserListener)
    }

    override fun onBindViewHolder(holder: FriendsListViewHolder, position: Int) {
        holder.bind(oldList.get(position))
    }

    override fun getItemCount() = oldList.size

    fun submitList(newList:ArrayList<User>){
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

    override fun cancelledRequest(user: User) {
        oldList.remove(user)
        mReference.child("${user.id}/friends/").orderByValue().equalTo(currentUser.id).addValueEventListener(object:ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                   snapshot.getValue(object:GenericTypeIndicator<HashMap<String,User>>(){})?.let{
                       val map = it
                       map.entries.forEach{
                           mReference.child("${user.id}/friends/${currentUser.id}").setValue(null)
                       }
                   }
            }
            override fun onCancelled(error: DatabaseError) {
            }
        })
    }
}

interface CancelTapped{
    fun cancelledRequest(user:User)
}