package com.example.justtalk.Kotlin.UI

import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.justtalk.Kotlin.Adapter.MessageThreadAdapter
import com.example.justtalk.Kotlin.models.ChatRef
import com.example.justtalk.Kotlin.models.Message
import com.example.justtalk.Kotlin.models.User
import com.example.justtalk.R
import com.example.justtalk.databinding.FragmentTalkBinding
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.tabs.TabLayout
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.ktx.storage

class TalkFragment : Fragment() {
    lateinit private var mBinding : FragmentTalkBinding
    lateinit private var mRoom : DatabaseReference  //Reference to messsage room
    private val mModel : MainActivityViewModel by activityViewModels()
    lateinit private var mAdapter : MessageThreadAdapter
    lateinit private var mUser: User  //Current User
    lateinit private var mChat : ChatRef //Current Reference to Chat
    private var listOfMessages = ArrayList<Message>()
    lateinit private var mEndUser: User
    lateinit private var mStorage : StorageReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mUser = mModel.mUser.value!!
        mChat = mModel._currentRoom.value!!
        getEndUser(mChat.freindId!!)
        mModel.setMessageList(ArrayList())
        mRoom = Firebase.database.reference.child("MessageRoom/${mChat.chatRoomId}/")
        if (savedInstanceState == null) {
            (activity as MainActivity).apply {
                findViewById<AppBarLayout>(R.id.appbar).visibility =
                    View.GONE
            }

        }
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
        getEndUser(mChat.freindId!!)
        Handler().postDelayed(object:Runnable{
            override fun run() {
                setUpAdapter()
            }
        },100)
        mBinding.backButton.setOnClickListener {
            parentFragmentManager.popBackStack()
        }

        mBinding.sendButton.setOnClickListener {
            val text = mBinding.messageFieldEditText.text.toString()
            if(!text.isEmpty()){
                val message = Message(mUser.id,text,System.currentTimeMillis().toString())
                mRoom.push().setValue(message)
                mBinding.messageFieldEditText.setText("")
            }
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
    fun setUpAdapter(){
        mAdapter = MessageThreadAdapter(mEndUser,mUser)
        mModel._messageList.observe(requireActivity()){
            mAdapter.submitList(it)
        }
        mBinding.recyclerViewMessage.apply{
            layoutManager = LinearLayoutManager(requireContext())
            adapter = mAdapter
        }
    }
    fun getEndUser(str:String){
        Firebase.database.reference.child("Users").orderByKey().equalTo(str).addValueEventListener(object:ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                snapshot.getValue(object:GenericTypeIndicator<HashMap<String,User>>(){})?.let {
                    it.values.forEach {
                        mEndUser = it
                        mStorage = Firebase.storage.reference.child("UserDP/${it.uid}.jpg")
                        mStorage.downloadUrl.addOnCompleteListener {task->
                            if(task.isComplete){
                                context?.let{
                                    Glide.with(it.applicationContext).load(task.result).into(mBinding.dpToolbar)
                                }
                            }
                        }
                        mBinding.toolbar.setTitle(it.name)

                    }
                }
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

    override fun onDestroy() {
        super.onDestroy()
        (activity as MainActivity).findViewById<AppBarLayout>(R.id.appbar).visibility = View.VISIBLE
    }
}