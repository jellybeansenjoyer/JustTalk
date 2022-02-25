package com.example.justtalk.Kotlin.UI

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.example.justtalk.R
import com.example.justtalk.databinding.FragmentCreateBinding
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class CreateFragment : Fragment() {
    lateinit private var mBinding : FragmentCreateBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

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
            val confpass = mBinding.passwordTiet2.text.toString()
            if(!email.isEmpty()&&!pass.isEmpty()&&!confpass.isEmpty()){
                if(pass.equals(confpass)){
                    mAuth.createUserWithEmailAndPassword(email,pass).addOnCompleteListener {
                        if(it.isSuccessful){
                            val res = it.result
                            (activity as AuthActivity).makeTransaction(InfoFragment::class)
                        }else{
                            Toast.makeText(requireActivity(), "User Creation Failed", Toast.LENGTH_SHORT).show()
                        }
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