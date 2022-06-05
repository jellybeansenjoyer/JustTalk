package com.example.justtalk.Kotlin.UI

import android.app.Activity.RESULT_OK
import android.content.Context.MODE_PRIVATE
import android.content.Intent
import android.content.Intent.ACTION_GET_CONTENT
import android.net.Uri
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
import androidx.core.content.edit
import androidx.core.view.isEmpty
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import com.bumptech.glide.Glide
import com.example.justtalk.Kotlin.models.User
import com.example.justtalk.R
import com.example.justtalk.databinding.FragmentInfoBinding
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.ktx.storage

//To add info of the newly created user
private const val TAG = "InfoFragment"
private const val PICK_IMAGE = 123
class InfoFragment() : Fragment() {

    private var fbuser: FirebaseUser?=null
    lateinit private var key:String
    lateinit private var password:String
    //Easy way to initialize viewmodels
    private val model : AuthViewModel by activityViewModels()
    //FirebaseStorage
    lateinit private var mStorage : StorageReference
    lateinit private var mReference: DatabaseReference

    private var registry: ActivityResultLauncher<Intent> = registerForActivityResult(ActivityResultContracts.StartActivityForResult(),
        object:ActivityResultCallback<ActivityResult>{
        override fun onActivityResult(result: ActivityResult?) {
            if(result!!.resultCode== RESULT_OK){
                //result.data is the T:Intent reslt.data.data is then the Intent.data = imageURI
                val imageUri = result.data!!.data!!
                //Set Global
                image = imageUri
                //Load to the UI
                Glide.with(requireActivity()).load(image.toString()).into(mBinding.userProfile)
            }
        }
    })

    lateinit private var mBinding : FragmentInfoBinding
    private var image: Uri? =null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        fbuser = Firebase.auth.currentUser!!
        mReference = Firebase.database.reference.child("Users")
        mStorage = Firebase.storage.reference.child("UserDP/${fbuser!!.uid}.jpg")
        arguments?.let{
            password = it.getString("InfoId")!!
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        mBinding = DataBindingUtil.inflate(inflater,R.layout.fragment_info,container,false)
        mBinding.createButton.isEnabled = true
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val auth = Firebase.auth.currentUser!!
        val fbuserId = auth.uid
        mBinding.createButton.setOnClickListener{
            it.isEnabled = false
            //FindFriendsActivity
            mBinding.progressBarInfo.visibility = View.VISIBLE
            val name = mBinding.nameFieldEditText.text.toString()
            //non-empty field check
            if(!name.isEmpty()){
                pushToDatabase(name)
//                Firebase.auth.signInWithEmailAndPassword(fbuser!!.email!!,password).addOnCompleteListener {
//                    if (it.isSuccessful){

                        //                            getUserAndUpdateVM(fbuserId,false)
//                    }
//                }
            } else
                Toast.makeText(requireContext(), "name is required", Toast.LENGTH_SHORT).show()
        }
        //On clicking the fab get picture from the phone's gallery
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

    //Update the user with the new information
    fun pushToDatabase(mName:String){
        //Getting Generated Key from Database
        key = mReference.push().key!!

        //Making user instance
        val id = key
        val name = mName
        val uid = fbuser!!.uid
        val email = fbuser!!.email.toString()
        val dp:String = image.toString()
        val mUser = User(id=id,name=name,uid=uid,email=email,dp=dp)


        //Setting the User object for the App in the ModelDatabase & the Key
        model.setUserKey(key)
        model.setUserValue(mUser)

        //Pushing the User object to database and the image to Storage Database
        mReference.updateChildren(hashMapOf(Pair<String,Any>(key,mUser)))
        mStorage.putFile(image!!).addOnSuccessListener {
            mBinding.progressBarInfo.visibility = View.INVISIBLE
            switchFragment()
        }
    }
    fun switchFragment(){
        (activity as AuthActivity).apply {
            val bundle = Bundle()
            bundle.putInt("FindFriendsId",0)
            makeTransaction(FindFriendsFragment::class,bundle,"replace")
        }
    }
}