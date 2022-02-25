package com.example.justtalk.Kotlin.UI

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.example.justtalk.Kotlin.models.User
import com.example.justtalk.R
import com.example.justtalk.databinding.FragmentInfoBinding
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class InfoFragment() : Fragment() {
    private var fbuser: FirebaseUser?=null
    lateinit private var mBinding : FragmentInfoBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        fbuser = Firebase.auth.currentUser!!
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        mBinding = DataBindingUtil.inflate(inflater,R.layout.fragment_info,container,false)
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mBinding.createButton.setOnClickListener{
            pushToDatabase()
            //FindFriendsActivity

        }
    }
    fun pushToDatabase(){
        val mReference = Firebase.database.reference.child("Users")
        val id = fbuser!!.uid
        val name = fbuser!!.displayName.toString()
        val email = fbuser!!.email.toString()
        val dp:String
        val bio:String
        val phno = fbuser!!.phoneNumber
        val mUser = User(id,name,email,dp,bio,phno,null,null,null)
        mReference.push().setValue(mUser)
    }
}