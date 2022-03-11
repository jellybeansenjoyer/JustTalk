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
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.GenericTypeIndicator
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

private const val TAG = "TestFirebaseFragment"
class TestFirebaseFragment : Fragment() {
    lateinit private var mBinding: FragmentTestFirebaseBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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
        val reference = Firebase.database.reference.child("UserTest")
        val key = reference.push().key!!
        mBinding.button.setOnClickListener{
            val k = reference.push().key
            reference.updateChildren(hashMapOf<String,Any>(Pair("${key}/test/${k}",mBinding.message.text.toString())))
        }
        mBinding.removeDataButton.setOnClickListener {
            reference.child("${key}/test/").orderByKey().addValueEventListener(object : ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    snapshot.getValue(object:GenericTypeIndicator<HashMap<String,String>>(){})?.let{
                        val map = it
                        map.entries.forEach{
                            if(it.value.equals(mBinding.removeValueEditText.text.toString())){
                                reference.child("${key}/test/${it.key}").setValue(null)
                            }
                        }
                    }
                }
                override fun onCancelled(error: DatabaseError) {
                }
            })
        }
    }
}