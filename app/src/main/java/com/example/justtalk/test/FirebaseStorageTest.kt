package com.example.justtalk.test

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.databinding.DataBindingUtil
import com.bumptech.glide.Glide
import com.example.justtalk.R
import com.example.justtalk.databinding.FragmentFirebaseStorageTestBinding
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.ktx.storage
import java.io.File

private const val TAG = "FirebaseStorageTest"
class FirebaseStorageTest : Fragment() {
    lateinit private var mBinding: FragmentFirebaseStorageTestBinding
    lateinit private var mStorage : StorageReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Firebase.auth.signInAnonymously().addOnCompleteListener {
            if(it.isSuccessful){
                Log.e(TAG,"successful")
            }else{
                Log.e(TAG,"failed")
                it.exception.apply{
                    throw this!!
                }
            }
        }
        mStorage = Firebase.storage.reference

    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        mBinding = DataBindingUtil.inflate(inflater,R.layout.fragment_firebase_storage_test , container, false)
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val registry = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
            if(it.resultCode==Activity.RESULT_OK){
                mBinding.progressBar.visibility = View.VISIBLE
                Log.e(TAG,"Callback")
                val image = it.data!!.data!!
                mStorage.child("Test/photo3.jpg")
                    .putFile(image).addOnCompleteListener{
                        if(it.isComplete){
                            Toast.makeText(this.context, "Upload Complete", Toast.LENGTH_SHORT).show()
                            mBinding.progressBar.visibility = View.INVISIBLE
                        }
                    }
            }
        }
        mBinding.retrieveImageButton.setOnClickListener {
            mBinding.progressBar.visibility = View.VISIBLE
            val image = mStorage.child("Test/photo3.jpg")
            image.downloadUrl.addOnCompleteListener {
                if(it.isComplete) {
                    Glide.with(this.context!!).load(it.result).into(mBinding.imageRecieverImageView)
                    mBinding.progressBar.visibility = View.INVISIBLE
                }
            }
        }
        mBinding.sendImageButton.setOnClickListener {
            val intent = Intent().apply{
                setAction(Intent.ACTION_GET_CONTENT)
                setType("image/*")
            }
            registry.launch(intent)
        }
    }
}