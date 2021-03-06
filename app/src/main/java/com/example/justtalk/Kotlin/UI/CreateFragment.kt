package com.example.justtalk.Kotlin.UI

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.edit
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import com.example.justtalk.R
import com.example.justtalk.databinding.FragmentCreateBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

//To create a new User for the app
private const val TAG = "CreateFragment"
class CreateFragment : Fragment() {
    lateinit private var mBinding : FragmentCreateBinding
    lateinit private var mAuth : FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mAuth = Firebase.auth
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        mBinding = DataBindingUtil.inflate(inflater,R.layout.fragment_create,container,false)
        return mBinding.root

    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mAuth = Firebase.auth
        mBinding.createButton2.setOnClickListener{

            val email = mBinding.emailTiet.text.toString()
            val pass = mBinding.passwordTiet.text.toString()
            val confirmpass = mBinding.passwordTiet2.text.toString()
            //Non Empty Field Check
            if(!email.isEmpty()&&!pass.isEmpty()&&!confirmpass.isEmpty()){
                //Password Mismatch check
                if(pass.equals(confirmpass)){
                    //CreateUser
                    mAuth.createUserWithEmailAndPassword(email,pass).addOnCompleteListener {
                        if(it.isSuccessful){
                            val bundle = Bundle()
                                bundle.putString("InfoId",pass)
                                (activity as AuthActivity).apply {
                                    val sharedPref = getSharedPreferences(AUTH_SHARED_PREFERENCE,
                                        Context.MODE_PRIVATE
                                    )
                                    sharedPref.edit{
                                        putString(EMAIL,email)
                                        putString(PASS,pass)
                                        putBoolean(LOGGED_IN,true)
                                    }
                                    makeTransaction(InfoFragment::class, bundle, "replace")
                                }
                        }else{
                            Toast.makeText(requireActivity(), "User Creation Failed", Toast.LENGTH_SHORT).show()
                        }
                    }.addOnFailureListener {
                        val stackTrace = it.stackTraceToString()
                        Log.e(TAG,stackTrace)
                    }
                }else{
                    Toast.makeText(requireContext(), "Passwords don't match", Toast.LENGTH_SHORT).show()
                }
            }else{
                Toast.makeText(requireContext(), "Fields cannot be empty", Toast.LENGTH_SHORT).show()
            }
        }
    }
}