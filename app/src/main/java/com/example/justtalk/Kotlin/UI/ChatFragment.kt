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
import com.example.justtalk.Kotlin.Adapter.ChatClickCallback
import com.example.justtalk.Kotlin.Adapter.ChatListAdapter
import com.example.justtalk.Kotlin.models.User
import com.example.justtalk.R
import com.example.justtalk.databinding.FragmentChatBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class ChatFragment() : Fragment(), ChatClickCallback {

    lateinit private var mBinding:FragmentChatBinding
    private val mViewModel:MainActivityViewModel by activityViewModels()
    lateinit private var user:User
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        user = mViewModel.mUser.value!!
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        val view = LayoutInflater.from(activity).inflate(R.layout.fragment_chat,container,false)
        mBinding = DataBindingUtil.bind(view)!!;
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
           val chats = user.chatrefs!!
            val adapter = ChatListAdapter(this)
                adapter.submitList(chats)
                mBinding.recyclerViewChat.apply{
                    this.adapter =adapter
                    this.layoutManager = LinearLayoutManager(requireContext())
                }
    }

    override fun onClick(user:User, view: View) {
        val bundle = Bundle()
        bundle.putString("abc123",user.id)
        (activity as MainActivity).makeTransactions(TalkFragment::class,bundle)
    }
}