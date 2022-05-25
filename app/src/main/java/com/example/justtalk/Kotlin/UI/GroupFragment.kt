package com.example.justtalk.Kotlin.UI

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.justtalk.Kotlin.Adapter.SelectListAdapter
import com.example.justtalk.Kotlin.models.ChatRef
import com.example.justtalk.Kotlin.models.User
import com.example.justtalk.R
import com.example.justtalk.databinding.FragmentGroupBinding
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class GroupFragment : Fragment() {
    lateinit private var mBinding:FragmentGroupBinding
    lateinit private var mList:ArrayList<User>
    private val model:MainActivityViewModel by activityViewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        mBinding = DataBindingUtil.inflate(inflater,R.layout.fragment_group,container,false)
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val adapter = SelectListAdapter()
        adapter.submitList(model.listOfFriends.value!!)
        mBinding.listOfFriends.apply{
            this.adapter = adapter
            this.layoutManager = LinearLayoutManager(this@GroupFragment.context)
        }
        mBinding.submitFab.setOnClickListener {
            mList = SelectListAdapter.SelectListViewHolder.mList
            writeToDatabase(mList)
            model.setGrpFriendsList(mList)
            //transfer to dialog to chat fragment
        }
    }
    fun writeToDatabase(groupMembers: List<User>){
        val reference = Firebase.database.reference.child("Groups")
        val refChat = Firebase.database.reference.child("GroupRoomRef")
        groupMembers.forEach {
            val master = reference.push().key!!
            reference.child("${it.uid}").run{
                updateChildren(hashMapOf(Pair<String,Any>(this.push().key!!,master)))
            }

        }
    }
}