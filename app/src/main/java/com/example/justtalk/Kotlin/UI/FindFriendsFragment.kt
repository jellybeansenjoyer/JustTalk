package com.example.justtalk.Kotlin.UI

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.justtalk.Kotlin.Adapter.FriendListAddFriendListener
import com.example.justtalk.Kotlin.models.User
import com.example.justtalk.R
import com.example.justtalk.databinding.FragmentFindFriendsBinding
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import java.time.temporal.ValueRange

private const val TAG = "FindFriendsFragment"
class FindFriendsFragment : Fragment() {
    lateinit private var mBinding : FragmentFindFriendsBinding
    lateinit private var reference: DatabaseReference
    lateinit private var user: User
    private var listOfPeople  = ArrayList<User>()
    private var listOfFriends : ArrayList<String> = ArrayList<String>()
    private val model : AuthViewModel by activityViewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        reference  = Firebase.database.reference.child("Users")
        user = model.mUser.value!!
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        mBinding = DataBindingUtil.inflate(inflater,R.layout.fragment_find_friends,container,false)
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        createList(listOfPeople)
        Log.e(TAG+"22",listOfPeople.size.toString())

        val mAdapter = FriendsListAdapter(object: FriendListAddFriendListener{
            override fun sendRequest(user: User) {
//                user.friendrefs!!.add(user.id!!)
                  listOfFriends.add(user.id!!)
                pushToDatabase()
            }
        })
        model.mUsers.observe(this){
            mAdapter.submitList(it)
        }
        mBinding.peopleRecyclerView.apply{
            this.adapter = mAdapter
            this.layoutManager = LinearLayoutManager(requireContext())
        }
    }
    fun createList(list:ArrayList<User>){
        reference.orderByKey().addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                val type = object:GenericTypeIndicator<HashMap<String,User>>(){}
                val result = snapshot.getValue(type)!!
                result.values.forEach{
                    list.add(it)
                }
                model.setList(list)
                Log.e(TAG,list.size.toString())
            }

            override fun onCancelled(error: DatabaseError) {

            }
        })
    }
    fun pushToDatabase(){
        //instead get the key to current user
        reference.orderByKey().addValueEventListener(object:ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                val type = object:GenericTypeIndicator<HashMap<String,User>>(){}
                val result = snapshot.getValue(type)!!
                result.values.forEach{
                    if(it.id==user.id){
                        val user = User(it.id,it.name,it.email,it.dp,friendrefs = listOfFriends)
                        val export = hashMapOf<String,Any>(Pair<String,User>(snapshot.key!!,user))
                        reference.updateChildren(export)
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
            }

        })
    }
}