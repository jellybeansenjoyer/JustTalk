package com.example.justtalk.Kotlin.UI

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.LinearLayout
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.justtalk.Kotlin.Adapter.ChatClickCallback
import com.example.justtalk.Kotlin.Adapter.ChatListAdapter
import com.example.justtalk.Kotlin.models.Chat
import com.example.justtalk.Kotlin.models.ChatRef
import com.example.justtalk.Kotlin.models.User
import com.example.justtalk.R
import com.example.justtalk.databinding.FragmentChatBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.protobuf.Value
import java.time.temporal.ValueRange

private const val TAG = "ChatFragment"
class ChatFragment() : Fragment(), ChatClickCallback {

    lateinit private var mBinding:FragmentChatBinding
    private val mViewModel:MainActivityViewModel by activityViewModels()
    lateinit private var mUser:User
    private var listOfPeople = ArrayList<User>() //List of Chats
    lateinit private var mReference : DatabaseReference //User info database

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mUser = mViewModel.mUser.value!!
        mReference = Firebase.database.reference.child("Users")
        if(mViewModel.listOfFriends.value==null)
            createList()
        else{
           listOfPeople =  mViewModel.listOfFriends.value!!
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        val view = LayoutInflater.from(activity).inflate(R.layout.fragment_chat,container,false)
        mBinding = DataBindingUtil.bind(view)!!
        val tabLayout = (activity as MainActivity).mBinding.tabLayout
        if(tabLayout.visibility==View.GONE){
            tabLayout.visibility = View.VISIBLE
        }
        val window = (activity as MainActivity).window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.statusBarColor = resources.getColor(R.color.marine_green)
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

            val adapter = ChatListAdapter(this)
                mViewModel.listOfFriends.observe(requireActivity()){
                    adapter.submitList(listOfPeople)
                }
                mBinding.recyclerViewChat.apply{
                    this.adapter = adapter
                    this.layoutManager = LinearLayoutManager(requireContext())
                }
    }

    override fun onClick(user:User, view: View,yesNo: Boolean) {
        Firebase.database.reference.child("ChatRoomRef/${mUser.id}").orderByKey().addValueEventListener(object:ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                snapshot.getValue(object:GenericTypeIndicator<HashMap<String,ChatRef>>(){})?.let{
                    it.entries.forEach{
                        if(it.value.freindId.equals(user.id)){
                            val result = it.value
                            mViewModel.setCurrentRoom(result)
                            (activity as MainActivity).apply{
                                makeTransactions(TalkFragment::class)
                                val parentBinding = this.mBinding
                                parentBinding.tabLayout.visibility = View.GONE
                            }
                            return
                        }
                    }
                }
            }
            override fun onCancelled(error: DatabaseError) {
            }
        })
    }
    fun createList(){
        Firebase.database.reference.child("ChatRoomRef/${mViewModel.mUser.value!!.id!!}/").addChildEventListener(object:ChildEventListener{
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                snapshot.getValue(ChatRef::class.java)?.let{
                    val result = it
                    val endUser = result.freindId
                    val chatRef = result.chatRoomId
                    mReference.orderByKey().equalTo(endUser).addValueEventListener(object:ValueEventListener{
                        override fun onDataChange(snapshot: DataSnapshot) {
                            snapshot.getValue(object:GenericTypeIndicator<HashMap<String,User>>(){})?.let{
                                it.values.forEach {
                                    it.chatroomref = chatRef
                                    listOfPeople.add(it)
                                }
                                mViewModel.setListOfFriends(listOfPeople)
                            }
                        }
                        override fun onCancelled(error: DatabaseError) {
                        }
                    })
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
//
//         Firebase.database.reference.child("ChatRoomRef/${mViewModel.mUser.value!!.id!!}/").orderByKey().addValueEventListener(object:ValueEventListener {
//            override fun onDataChange(snapshot: DataSnapshot) {
//                snapshot.getValue(object : GenericTypeIndicator<HashMap<String, ChatRef>>(){})?.let{
//                    it.entries.forEach{
//                        val result = it.value
//                        val endUser = result.freindId
//                        val chatRef = result.chatRoomId
//                        mReference.orderByKey().equalTo(endUser).addValueEventListener(object:ValueEventListener{
//                            override fun onDataChange(snapshot: DataSnapshot) {
//                                snapshot.getValue(object:GenericTypeIndicator<HashMap<String,User>>(){})?.let{
//                                    it.values.forEach {
//                                        it.chatroomref = chatRef
//                                        listOfPeople.add(it)
//                                    }
//                                    mViewModel.setListOfFriends(listOfPeople)
//                                }
//                            }
//                            override fun onCancelled(error: DatabaseError) {
//                            }
//                        })
//                    }
//                }
//            }
//            override fun onCancelled(error: DatabaseError) {
//            }
//        })
    }


}
