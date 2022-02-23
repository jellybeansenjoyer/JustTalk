package com.example.justtalk.Kotlin.UI

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.example.justtalk.Kotlin.models.Chat
import com.example.justtalk.Kotlin.models.Message
import com.example.justtalk.Kotlin.models.User
import com.example.justtalk.R
import com.example.justtalk.databinding.FragmentTalkBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase


class TalkFragment(private var user:User) : Fragment() {
    lateinit private var messageRoomId:String
    lateinit private var messageRoom: Chat
    lateinit private var mBinding:FragmentTalkBinding
    lateinit private var mReference:DatabaseReference
    lateinit private var friend:User
    lateinit private var messageThread:List<Message>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            messageRoomId = it.getString("abc123")!!
        }
        mReference = Firebase.database.reference.child("MessageRoom")
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        mBinding = DataBindingUtil.inflate(inflater,R.layout.fragment_talk,container,false)
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
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

    }
}