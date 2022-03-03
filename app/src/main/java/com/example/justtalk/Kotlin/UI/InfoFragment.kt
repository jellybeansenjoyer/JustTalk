package com.example.justtalk.Kotlin.UI

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.content.Intent.ACTION_GET_CONTENT
import android.os.Bundle
import android.provider.LiveFolders.INTENT
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContract
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.rememberUpdatedState
import androidx.core.view.isEmpty
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import com.bumptech.glide.Glide
import com.example.justtalk.Kotlin.models.User
import com.example.justtalk.R
import com.example.justtalk.databinding.FragmentInfoBinding
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

private const val TAG = "InfoFragment"
private const val PICK_IMAGE = 123
class InfoFragment() : Fragment() {
    private var fbuser: FirebaseUser?=null
    private var registry: ActivityResultLauncher<Intent> = registerForActivityResult(ActivityResultContracts.StartActivityForResult(),object:ActivityResultCallback<ActivityResult>{
        override fun onActivityResult(result: ActivityResult?) {
            if(result!!.resultCode== RESULT_OK){
                image = result.data!!.data.toString()
                Glide.with(requireActivity()).load(image).into(mBinding.userProfile)
            }
        }
    })
    private val viewModel :AuthViewModel by activityViewModels()
    lateinit private var mBinding : FragmentInfoBinding
    private var image: String? =null
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
        val auth = Firebase.auth.currentUser!!
        mBinding.createButton.setOnClickListener{
            //FindFriendsActivity
            val name = mBinding.nameFieldEditText.text.toString()
            if(!name.isEmpty()){
                pushToDatabase(name)
                (activity as AuthActivity).apply {
                    getUser(auth.uid)
                    makeTransaction(FindFriendsFragment::class,null,"replace")
//                    transferData()
                }
            } else
                Toast.makeText(requireContext(), "name is required", Toast.LENGTH_SHORT).show()
        }

        mBinding.addPictureFab.setOnClickListener{
            getImageFromGallery()
        }
    }

    fun getImageFromGallery(){
        val intent = Intent()
        intent.setType("image/*")
        intent.setAction(ACTION_GET_CONTENT)
        registry.launch(intent)
    }

    fun pushToDatabase(name:String){
        val mReference = Firebase.database.reference.child("Users")
        val id = fbuser!!.uid
        val name = name
        val email = fbuser!!.email.toString()
        val dp:String = image!!
        val mUser = User(id=id,name=name,email=email,dp=dp)
        mReference.push().setValue(mUser)
    }
}