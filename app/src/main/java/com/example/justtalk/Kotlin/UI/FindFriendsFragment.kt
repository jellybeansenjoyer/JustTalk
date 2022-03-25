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
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import java.time.temporal.ValueRange

private const val TAG = "FindFriendsFragment"
class FindFriendsFragment : Fragment() {

    lateinit private var mBinding : FragmentFindFriendsBinding
    lateinit private var mReference: DatabaseReference
    lateinit private var mReqReference: DatabaseReference
    lateinit private var mUser: User
    lateinit private var mCurrentUser : FirebaseUser
    private var listOfPeople  = ArrayList<User>()
    private val model : AuthViewModel by activityViewModels()
    private val mViewModel : MainActivityViewModel by activityViewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mReference  = Firebase.database.reference.child("Users")
        mReqReference = Firebase.database.reference.child("RequestRoom")
        arguments?.let{
            val id = it.getInt("FindFriendsId")
            when(id){
                0->{
                    mUser = model.mUser.value!!
                    mCurrentUser = Firebase.auth.currentUser!!
                }
                1->{
                    mUser = mViewModel.mUser.value!!
                }
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        mBinding = DataBindingUtil.inflate(inflater,R.layout.fragment_find_friends,container,false)
        arguments?.let{
            if(it.getInt("FindFriendsId")==1){
                mBinding.continueToMain.visibility = View.INVISIBLE
            }
        }
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        createList(listOfPeople)

        val mAdapter = FriendsListAdapter(object: FriendListAddFriendListener{
            override fun sendRequest(user: User) {
//                user.friendrefs!!.add(user.id!!)
                  val k = mReqReference.push().key
                  mReqReference.updateChildren(hashMapOf(Pair<String,Any>("${user.id!!}/friends/${k}",mUser.id!!)))

            }
        })

        model.mUsers.observe(this){
            mAdapter.submitList(it)
        }

        mBinding.peopleRecyclerView.apply{
            this.adapter = mAdapter
            this.layoutManager = LinearLayoutManager(requireContext())
        }

        mBinding.continueToMain.setOnClickListener{
            (activity as AuthActivity).apply{
                getUserAndUpdateVM(mCurrentUser.uid,true)
            }
        }
    }

    fun createList(list:ArrayList<User>){
        mReference.orderByKey().addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                val type = object : GenericTypeIndicator<HashMap<String, User>>() {}
                val result = snapshot.getValue(type)!!
                result.entries.forEach {
                    if(!it.value.id.equals(mUser.id))
                        list.add(it.value)
                }
                    model.setList(list)
            }
            override fun onCancelled(error: DatabaseError) {
            }
        })
    }

}