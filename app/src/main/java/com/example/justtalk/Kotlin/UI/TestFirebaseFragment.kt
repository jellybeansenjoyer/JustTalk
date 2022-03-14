package com.example.justtalk.Kotlin.UI

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.compose.ui.graphics.Outline
import androidx.databinding.DataBindingUtil
import com.example.justtalk.R
import com.example.justtalk.databinding.FragmentTestFirebaseBinding
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

private const val TAG = "TestFirebaseFragment"
class TestFirebaseFragment : Fragment() {
    lateinit private var mBinding: FragmentTestFirebaseBinding
    lateinit private var mReference : DatabaseReference
    lateinit private var key : String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mReference = Firebase.database.reference.child("Test")
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        mBinding = DataBindingUtil.inflate(inflater,R.layout.fragment_test_firebase,container,false)
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

       mBinding.removeDataButton.setOnClickListener{
           removeData()
       }
        mBinding.button.setOnClickListener {
            pushData()
        }
        mBinding.orderDataButton.setOnClickListener {
            orderData()
        }
        mBinding.orderChildDataButton.setOnClickListener {
            orderDataByChild()
        }
    }
    fun orderDataByChild(){
       mReference.child("Friend").addChildEventListener(object:ChildEventListener{
           override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
               snapshot.getValue(String::class.java).let{

                   Log.e(TAG,it.toString())
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
    }
    fun orderData(){
        mReference.child("Friend").orderByKey().addValueEventListener(object:ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                snapshot.getValue(object:GenericTypeIndicator<HashMap<String,String>>(){})?.let{
                    Log.e(TAG,it.entries.size.toString())
                }
            }
            override fun onCancelled(error: DatabaseError) {
            }

        })
    }
    fun pushData(){
        key = mReference.push().key!!
        mReference.child("Friend").updateChildren(hashMapOf<String,Any>(Pair(key,"Raghav")))
    }
    fun removeData(){
        mReference.child("Friend/${key}/").setValue(null)
    }
}