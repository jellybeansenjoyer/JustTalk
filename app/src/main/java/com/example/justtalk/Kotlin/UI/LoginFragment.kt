package com.example.justtalk.Kotlin.UI

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.example.justtalk.R
import com.example.justtalk.databinding.FragmentLoginBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

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
                mAuth.signInWithEmailAndPassword(email,pass).addOnCompleteListener {
                    if(it.isSuccessful){
                        val result = it.result
                        val currentUser = result.user!!
                        (activity as AuthActivity).apply {
                            getUser(currentUser.uid)
                            transferData()
                        }
                    }else{
                        Toast.makeText(requireContext(), "Login Failure", Toast.LENGTH_SHORT).show()
                    }
                }
            }else{
                Toast.makeText(activity, "Fields cannot be empty", Toast.LENGTH_SHORT).show()
            }
        }

    }

}