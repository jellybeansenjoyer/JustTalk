package com.example.justtalk.Kotlin.UI

import android.content.Context.MODE_PRIVATE
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
import androidx.lifecycle.lifecycleScope
import com.example.justtalk.R
import com.example.justtalk.databinding.FragmentLoginBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.*

//To LogIn the User into the app
class LoginFragment : Fragment() {
    lateinit private var mBinding:FragmentLoginBinding
    lateinit private var mAuth:FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mAuth = Firebase.auth
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        mBinding = DataBindingUtil.inflate(inflater,R.layout.fragment_login,container,false)
        return mBinding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mBinding.loginButton.setOnClickListener{
            val email = mBinding.emailTiet.text.toString()
            val pass = mBinding.passwordTiet.text.toString()
            if(!email.isEmpty()&&!pass.isEmpty()){
                firebaseLogin(email,pass)
            }else{
                Toast.makeText(activity, "Fields cannot be empty", Toast.LENGTH_SHORT).show()
            }
        }
        //Replace this fragment with the createFragment
        mBinding.createButton.setOnClickListener{
            (activity as AuthActivity).makeTransaction(CreateFragment::class,null,"replace")
        }
    }

    fun firebaseLogin(email:String,pass:String){
        mAuth.signInWithEmailAndPassword(email,pass).addOnCompleteListener {
            if(it.isSuccessful){
                val result = it.result
                val currentUser = result.user!!
                //Update the User in VM and in AuthActivity
                (activity as AuthActivity).apply {
                    val sharedPref = getSharedPreferences(AUTH_SHARED_PREFERENCE,MODE_PRIVATE)
                    sharedPref.edit{
                        putString(EMAIL,email)
                        putString(PASS,pass)
                        putBoolean(LOGGED_IN,true)
                    }
                    getUserAndUpdateVM(currentUser.uid,true)
                }
            }else{
                Toast.makeText(requireActivity(), "Login Failure", Toast.LENGTH_SHORT).show()
            }
        }
    }
}