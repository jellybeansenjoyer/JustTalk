package com.example.justtalk.test

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.example.justtalk.R
import com.example.justtalk.databinding.ActivityFirebaseTestBinding
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

private const val TAG = "FirebaseTestActivity"
class FirebaseTestActivity : AppCompatActivity() {
    lateinit private var mBinding: ActivityFirebaseTestBinding
    lateinit private var mReference: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = DataBindingUtil.setContentView(this,R.layout.activity_firebase_test)
        mReference = Firebase.database.reference.child("test")
        mBinding.press.setOnClickListener{
            val str = mBinding.field.text.toString()
            mReference.push().setValue(str)
        }
        mBinding.field.setOnClickListener {
            val str = mBinding.orderField.text.toString()
//            mReference.orderByKey().equalTo(str).get().addOnCompleteListener {
//                val res = it.result.getValue(String::class.java)
//                Log.e(TAG,res!!)
//                Toast.makeText(this, res!!, Toast.LENGTH_SHORT).show()
//            }
        }

//        mReference.orderByKey().addValueEventListener(object:ValueEventListener{
//            override fun onDataChange(snapshot: DataSnapshot) {
//                val gti = object:GenericTypeIndicator<HashMap<String,String>>(){}
//
//                snapshot.getValue(gti)!!.run {
//                    this.values.forEach {
//                        Log.e(TAG,it.toString())
//                    }
//                }
//            }
//
//            override fun onCancelled(error: DatabaseError) {
//
//            }
//
//        })
//            mReference.orderByChild("").addValueEventListener(object:ValueEventListener{
//                override fun onDataChange(snapshot: DataSnapshot) {
//                    snapshot.getValue(HashMap)
//                }
//
//                override fun onCancelled(error: DatabaseError) {
//
//                }
//
//            })
    }
}