package com.example.justtalk.Kotlin.UI

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.justtalk.Kotlin.Adapter.MessageThreadAdapter
import com.example.justtalk.Kotlin.models.Chat
import com.example.justtalk.Kotlin.models.Message
import com.example.justtalk.Kotlin.models.User
import com.example.justtalk.R
import com.example.justtalk.databinding.FragmentTalkBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase


class TalkFragment(private var user:User) : Fragment() {
    lateinit private var messageRoomId:String
    lateinit private var messageRoom: Chat
    lateinit private var mBinding:FragmentTalkBinding
    lateinit private var mReference:DatabaseReference
    lateinit private var friend:User
    lateinit private var messageThread:List<Message>
    lateinit private var mReference2:DatabaseReference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            messageRoomId = it.getString("abc123")!!
        }
        mReference = Firebase.database.reference.child("MessageRoom")
        mReference2 = Firebase.database.reference.child("Users")
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        mBinding = DataBindingUtil.inflate(inflater,R.layout.fragment_talk,container,false)
        (activity as MainActivity).setSupportActionBar(mBinding.toolbar)
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        messageRoom = searchMessage(messageRoomId)!!
        messageThread = messageRoom.messages
        var protagonist:String
        var friend:String
        if(user.name.equals(messageRoom.sender)){
            protagonist = user.name
            friend = messageRoom.reciever
        }else{
            protagonist = messageRoom.reciever
            friend = user.name
        }
        val us = search(protagonist)!!
        val them = search(friend)!!

        Glide.with(requireActivity()).load(them.dp).into(mBinding.dpToolbar)
        mBinding.toolbar.setTitle(them.name)
        mBinding.recyclerViewMessage.apply {
            val mAdapter = MessageThreadAdapter()
            mAdapter.submitList(messageThread)
            adapter = mAdapter
            layoutManager = LinearLayoutManager(activity)
        }
    }
    fun search(value:String):User?{
        var user:User? = null
        mReference2.orderByChild("id").equalTo(value).addValueEventListener(object:
            ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                snapshot.children.forEach {
                    val result = it.getValue(User::class.java)
                    user = result
                }
            }
            override fun onCancelled(error: DatabaseError) {

            }
        })
        return user
    }

    fun searchMessage(value:String):Chat?{
        var chat:Chat? = null
        mReference2.orderByChild("id").equalTo(value).addValueEventListener(object:
            ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                snapshot.children.forEach {
                    val result = it.getValue(Chat::class.java)
                    chat = result
                }
            }
            override fun onCancelled(error: DatabaseError) {

            }
        })
        return chat
    }
    }



