package com.example.justtalk.Kotlin.UI

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.graphics.Outline
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.justtalk.Kotlin.Adapter.ChatClickCallback
import com.example.justtalk.Kotlin.models.Chat
import com.example.justtalk.Kotlin.models.ChatRef
import com.example.justtalk.Kotlin.models.User
import com.example.justtalk.R
import com.example.justtalk.databinding.FragmentFriendsBinding
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class FriendsFragment : Fragment(),ChatClickCallback {
    lateinit private var mBinding: FragmentFriendsBinding
    lateinit private var mReference: DatabaseReference
    private val model : MainActivityViewModel by activityViewModels()
    lateinit private var mUser : User
    private val listOfRequestsUser:ArrayList<User> = ArrayList()
    private val listOfRequests:ArrayList<String> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mUser = model.mUser.value!!
        mReference = Firebase.database.reference.child("Users")
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        mBinding = DataBindingUtil.inflate(inflater,R.layout.fragment_friends,container,false)
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mAdapter = FriendsAdapter(mUser,this)
        createList()
        model.listOfRequests.observe(requireActivity()){
            mAdapter.submitList(listOfRequestsUser)
        }
        mBinding.peopleRecyclerView.apply  {
            layoutManager = LinearLayoutManager(requireActivity())
            adapter = mAdapter
        }
        mBinding.moveToMain.setOnClickListener{
            (activity as MainActivity).makeTransactions(ChatFragment::class,null)
        }
    }

    fun createList(){
        Firebase.database.reference.child("RequestRoom/${mUser.id!!}/friends").orderByValue().addValueEventListener(object:
            ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                snapshot.getValue(object: GenericTypeIndicator<HashMap<String, String>>(){})?.let{
                    it.values.forEach {
                        Firebase.database.reference.child("Users").orderByKey().equalTo(it).addValueEventListener(
                            object:ValueEventListener{
                                override fun onDataChange(snapshot: DataSnapshot) {
                                    snapshot.getValue(object: GenericTypeIndicator<HashMap<String, User>>(){})?.let{
                                        it.values.forEach {
                                            listOfRequestsUser.add(it)
                                        }
                                        model.setListOfRequests(listOfRequestsUser)
                                    }
                                }
                                override fun onCancelled(error: DatabaseError) {
                                }
                            }
                        )
                    }
                }
            }
            override fun onCancelled(error: DatabaseError) {
            }

        })
    }

    override fun onClick(user: User, view: View) {
        val ref = Firebase.database.reference
        val chatRoomRef = ref.child("ChatRoomRef")
        val messageRoom = ref.child("MessageRoom")
        val ssid = chatRoomRef.push().key!!
        val chatObjA = ChatRef(user.id!!,ssid)
        val chatObjB = ChatRef(mUser.id!!,ssid)
        val uniqueIdA = chatRoomRef.push().key!!
        val uniqueIdB = chatRoomRef.push().key!!
        chatRoomRef.updateChildren(hashMapOf<String,Any>(Pair("${mUser.id!!}/${uniqueIdA}",chatObjA),Pair("${user.id!!}/${uniqueIdB}",chatObjB)))
    }
}