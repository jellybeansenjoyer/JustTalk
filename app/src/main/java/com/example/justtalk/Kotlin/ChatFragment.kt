package com.example.justtalk.Kotlin

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.example.justtalk.Kotlin.models.User
import com.example.justtalk.R
import com.example.justtalk.databinding.FragmentChatBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class BlankFragment : Fragment(),ChatClickCallback {

    lateinit private var mBinding:FragmentChatBinding
    lateinit private var auth:FirebaseAuth
    lateinit private var referenceUser: DatabaseReference
    lateinit private var user: User
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = Firebase.auth
        referenceUser = Firebase.database.reference.child("Users")

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
           val userFriends = user.friendrefs;

    }

    override fun onClick(userId:String, view: View) {
        val bundle = Bundle()
        bundle.putString("abc123",userId)
        (activity as MainActivity).makeTransactions(Fragment::class,bundle)
    }
}