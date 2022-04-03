package com.example.justtalk.Kotlin.UI

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
import androidx.activity.result.contract.ActivityResultContract
import androidx.activity.result.contract.ActivityResultContracts
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import com.bumptech.glide.Glide
import com.example.justtalk.Kotlin.models.User
import com.example.justtalk.R
import com.example.justtalk.databinding.FragmentUserProfileBinding
import com.google.android.gms.tasks.Task
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.ktx.storage

private const val TAG = "FragmentUserProfile"
class FragmentUserProfile : Fragment() {
    lateinit private var mBinding : FragmentUserProfileBinding
    private val mModel:MainActivityViewModel by activityViewModels()
    lateinit private var mReference: DatabaseReference
    lateinit private var mUser: User
    lateinit private var mStorage:StorageReference
    private var mUrl: Uri? = null
    private val cb = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
        if(it.resultCode==Activity.RESULT_OK){
            mUrl = it.data!!.data!!
            Glide.with(this.context!!).load(mUrl.toString()).into(mBinding.dpUser)
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mReference = Firebase.database.reference.child("Users")
        mUser = mModel.mUser.value!!
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        mBinding = DataBindingUtil.inflate(inflater,R.layout.fragment_user_profile,container,false)
        mBinding.NameUser.setText(mUser.name!!)
        mStorage = Firebase.storage.reference.child("UserDP/${mUser.uid!!}.jpg")
        mStorage.downloadUrl.addOnSuccessListener {
            Glide.with(this.context!!).load(it).into(mBinding.dpUser)
        }
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mBinding.changePicture.setOnClickListener {
            changeDp()
        }
        mBinding.commitChanges.setOnClickListener {
            var a:String?= null
            var b:Uri?= null
            if(!mUser.name!!.equals(mBinding.NameUser.text.toString())){
                a = mBinding.NameUser.text.toString()
                Log.e(TAG,"Name changed")
            }
            mUrl?.let{
                b = it
                Log.e(TAG,"Dp changed")
            }
            changeWord(a,b)
        }
    }
    fun changeWord(word:String?=null,dp:Uri?=null) {
        if (word == null && dp == null)
            return
        Log.e(TAG,word!!)
        var user: User? = null
        mReference.orderByKey().equalTo(mUser.id!!)
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    snapshot.getValue(object : GenericTypeIndicator<HashMap<String, User>>() {})
                        ?.let {
                            it.entries.forEach {
                                user = it.value
                                word?.let {
                                    user!!.name = it
                                }
                                dp?.let {
                                    user!!.dp = it.toString()
                                }
                            }

//                            mReference.updateChildren(
//                                hashMapOf<String,Any>(
//                                    Pair<String, User>(
//                                        mUser.id!!,
//                                        user!!
//                                    )
//                                )
//                            ).addOnFailureListener {
//                                Log.e(TAG,it.toString())
//                            }
                        }
                }

                override fun onCancelled(error: DatabaseError) {
                }
            })
        dp?.let {
            mStorage.apply {
                    putFile(mUrl!!).addOnCompleteListener {
                        if (it.isSuccessful) {
                            Toast.makeText(
                                this@FragmentUserProfile.context!!,
                                "uploadedSuccessfully",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                }
            }
    }
    fun changeDp(){
        val intent = Intent()
        intent.setAction(Intent.ACTION_GET_CONTENT)
        intent.setType("image/*")
        cb.launch(intent)
    }
}