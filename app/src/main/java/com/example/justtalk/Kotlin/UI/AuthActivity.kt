package com.example.justtalk.Kotlin.UI

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.example.justtalk.Kotlin.models.User
import com.example.justtalk.R
import com.example.justtalk.databinding.ActivityAuthBinding
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class AuthActivity : AppCompatActivity() {

    lateinit private var mBinding : ActivityAuthBinding
    lateinit private var fUser: FirebaseUser
    private var mUser: User? = null
    lateinit private var mReference: DatabaseReference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = DataBindingUtil.setContentView(this,R.layout.activity_auth)
        mReference = Firebase.database.reference.child("Users")
        val auth = Firebase.auth

        var fUserId:String? = null
        if(auth.currentUser!=null){
            fUser = auth.currentUser!!
            fUserId = fUser.uid
            getUser(fUserId)
        }else{
            //Make authentication

        }



    }

    fun transferData(){
        val intent = Intent()
        intent.putExtra("AuthActivity",mUser)
        startActivityForResult(intent,123)
    }
    fun getUser(fUserId:String){
        mReference.orderByChild("Id").equalTo(fUserId).addValueEventListener(object:
            ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                snapshot.children.forEach {
                    val result = it.getValue(User::class.java)
                    mUser = result
                }
            }
            override fun onCancelled(error: DatabaseError) {
            }
        })
    }
}