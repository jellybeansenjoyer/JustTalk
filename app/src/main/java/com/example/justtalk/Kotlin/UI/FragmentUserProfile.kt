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
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.example.justtalk.Kotlin.models.User
import com.example.justtalk.R
import com.example.justtalk.databinding.ActivityMainBinding
import com.example.justtalk.databinding.FragmentUserProfileBinding
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.ktx.storage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

private const val TAG = "FragmentUserProfile"
class FragmentUserProfile : Fragment(),InterceptProgress {
    lateinit private var mBinding : FragmentUserProfileBinding
    private val mModel:MainActivityViewModel by activityViewModels()
    lateinit private var mReference: DatabaseReference
    lateinit private var mUser: User
    lateinit private var mStorage:StorageReference
    private var mUrl: Uri? = null
    private val cb = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
        if(it.resultCode==Activity.RESULT_OK){
            mUrl = it.data!!.data!!
            Glide.with(this.context!!.applicationContext).load(mUrl).into(mBinding.dpUser)
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
        mStorage.downloadUrl.addOnCompleteListener {
            if(it.isSuccessful) {
                this.context?.let {
                    Glide.with(it.applicationContext).load(it).into(mBinding.dpUser)
                }
            }
        }
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mBinding.changePicture.setOnClickListener {
            it.isEnabled = false
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
                            mReference.updateChildren(hashMapOf<String,Any>(Pair<String, User>(mUser.id!!, user!!)))
                        }
                }

                override fun onCancelled(error: DatabaseError) {
                }
            })
        dp?.let {
            mStorage.putFile(mUrl!!).apply{
                progress(isInProgress)
                addOnCompleteListener {

                if (it.isSuccessful) {
                    Toast.makeText(
                        this@FragmentUserProfile.context!!,
                        "uploadedSuccessfully",
                        Toast.LENGTH_SHORT
                    ).show()
                    progress(!isInProgress)
                    mBinding.commitChanges.isEnabled = true
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

    override fun progress(flag: Boolean) {
        if(flag)
            mBinding.progressBar.visibility = View.VISIBLE
        else
            mBinding.progressBar.visibility = View.INVISIBLE

    }
}
interface InterceptProgress{
    fun progress(flag:Boolean)
}