package com.example.justtalk.Kotlin.UI

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.justtalk.Kotlin.Adapter.MessageThreadAdapter
import com.example.justtalk.Kotlin.models.ChatRef
import com.example.justtalk.Kotlin.models.Message
import com.example.justtalk.Kotlin.models.User
import com.example.justtalk.R
import com.example.justtalk.databinding.FragmentTalkBinding
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class TalkFragment : Fragment() {
    lateinit private var mBinding : FragmentTalkBinding
    lateinit private var mRoom : DatabaseReference
    private val mModel : MainActivityViewModel by activityViewModels()
    lateinit private var mUser: User
    lateinit private var mChat : ChatRef
    private var listOfMessages = ArrayList<Message>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mUser = mModel.mUser.value!!
        mChat = mModel._currentRoom.value!!
        mModel.setMessageList(ArrayList())
        mRoom = Firebase.database.reference.child("MessageRoom/${mChat.chatRoomId}/")
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_talk,container,false)
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mAdapter = MessageThreadAdapter()
        mModel._messageList.observe(requireActivity()){
            mAdapter.submitList(it)
        }
        mBinding.backButton.setOnClickListener {
            parentFragmentManager.popBackStack()
        }
        mBinding.sendButton.setOnClickListener {
            val text = mBinding.messageFieldEditText.text.toString()
            if(!text.isEmpty()){
                val message = Message(text,System.currentTimeMillis().toString())
                mRoom.push().setValue(message)
                mBinding.messageFieldEditText.setText("")
            }
        }
        mBinding.recyclerViewMessage.apply{
            layoutManager = LinearLayoutManager(requireContext())
            adapter = mAdapter
        }

        mRoom.addChildEventListener(object: ChildEventListener{
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                snapshot.getValue(Message::class.java)?.let{
                    listOfMessages.add(it)
                    mModel.setMessageList(listOfMessages)
                }
            }
            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
            }
            override fun onChildRemoved(snapshot: DataSnapshot) {
            }
            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {
            }
            override fun onCancelled(error: DatabaseError) {
            }
        })
    }

    fun loadMessageList(){
        mRoom.orderByKey().addValueEventListener(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                snapshot.getValue(object:GenericTypeIndicator<HashMap<String,Message>>(){})?.let{
                    it.values.forEach {
                        listOfMessages.add(it)
                    }
                    mModel.setMessageList(listOfMessages)
                }
            }
            override fun onCancelled(error: DatabaseError) {
            }
        })
    }

    override fun onStop() {
        super.onStop()
        mModel.setListOfFriends(ArrayList())

    }
    override fun onPause() {
        super.onPause()
        mModel.setListOfFriends(ArrayList())

    }
    override fun onDestroy() {
        super.onDestroy()
        mModel.setListOfFriends(ArrayList())
    }
}