package com.example.justtalk.Kotlin.Adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.justtalk.Kotlin.models.*
import com.example.justtalk.R
import com.example.justtalk.databinding.ModelChatBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.ktx.storage
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.HashMap

private const val TAG = "ChatListAdapter"
class ChatListAdapter(private val mListener: ChatClickCallback,private val mUser:User) : RecyclerView.Adapter<ChatListAdapter.ChatViewHolder>() {
    private var oldList:List<ChatFrag> = emptyList()
    private var mReference: DatabaseReference
    init {
        mReference = Firebase.database.reference.child("Users")
    }
    class ChatViewHolder(private val view: View,private val context: Context,private val mListener: ChatClickCallback,private val mUser:User):RecyclerView.ViewHolder(view){
        private var mBinding : ModelChatBinding
        lateinit private var mStorage: StorageReference
        init{
            mBinding = DataBindingUtil.bind(view)!!
            mStorage = Firebase.storage.reference
        }

        fun bind(user:ChatFrag){
            when(user){
                is User->{
                    mStorage.child("UserDP/${user.uid}.jpg").downloadUrl.addOnCompleteListener {
                        if(it.isComplete){
                            Glide.with(context.applicationContext).load(it.result).into(mBinding.dpUser)
                        }
                    }
                    mBinding.nameUser.setText(user.name)
                    mBinding.card.setOnClickListener{
                        mListener.onClick(user,view,false)
                    }
                    getTheLastText(user,mBinding.lastMessageTextView,mBinding.timestampUser)

                }
                   is Group ->{
                       mStorage.child("GroupDP/${user.dp}").downloadUrl.addOnCompleteListener {
                           if(it.isSuccessful){
                               Glide.with(context.applicationContext).load(it.result).into(mBinding.dpUser)
                           }
                       }
                       mBinding.nameUser.setText(user.name)
                       mBinding.card.setOnClickListener{
                           mListener.onClick(user,view,false)
                       }
                }
            }
        }
        fun makeDate(time:Long):String{
            val date = Date(time)
            val formatter = SimpleDateFormat("dd/MM/yyyy")
            return formatter.format(date)
        }
        fun getTheLastText(user:User,textView: TextView,dateTextView:TextView){
            Firebase.database.reference.child("ChatRoomRef/${mUser.id}").orderByKey().addValueEventListener(object:ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    snapshot.getValue(object:GenericTypeIndicator<HashMap<String, ChatRef>>(){})?.let{
                        it.values.forEach {
                            if(it.freindId.equals(user.id)){
                                Firebase.database.reference.child("MessageRoom/${it.chatRoomId}").orderByKey().limitToLast(1).addValueEventListener(object:ValueEventListener{
                                    override fun onDataChange(snapshot: DataSnapshot) {
                                        snapshot.getValue(object:GenericTypeIndicator<HashMap<String, Message>>(){})?.let{
                                            Log.e(TAG+"blah",it.size.toString())
                                            it.values.last().apply{
                                                if(this.content!!.length<50)
                                                textView.setText(this.content)
                                                else{
                                                    textView.setText(this.content!!.substring(0,30)+"...")
                                                }
                                                Log.e(TAG,content!!)
                                                val date = makeDate(this.timestamp!!.toLong())
                                                dateTextView.setText(date)
                                            }
                                        }
                                    }
                                    override fun onCancelled(error: DatabaseError) {
                                    }
                                })
                            }
                        }
                    }
                }
                override fun onCancelled(error: DatabaseError) {
                }
            })
        }

        companion object{
            fun getInstance(parent:ViewGroup,mListener: ChatClickCallback,user:User): ChatViewHolder {
                val view = LayoutInflater.from(parent.context).inflate(R.layout.model_chat,parent,false)
                return ChatViewHolder(view,parent.context,mListener,user)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatViewHolder {
        return ChatViewHolder.getInstance(parent, mListener,mUser);
    }

    override fun onBindViewHolder(holder: ChatViewHolder, position: Int) {
        holder.bind(oldList[position])
    }

    override fun getItemCount() = oldList.size

     fun submitList(newList:List<ChatFrag>){
        val diffutil = DiffUtilChat(oldList,newList)
        val calcdiff = DiffUtil.calculateDiff(diffutil)
        oldList = newList
        calcdiff.dispatchUpdatesTo(this)
    }
}