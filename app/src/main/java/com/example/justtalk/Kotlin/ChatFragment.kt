package com.example.justtalk.Kotlin

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.layout.Layout
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.example.justtalk.R
import com.example.justtalk.databinding.FragmentChatBinding
import kotlinx.coroutines.NonDisposableHandle.parent

class BlankFragment : Fragment() {

    lateinit private var mBinding:FragmentChatBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = LayoutInflater.from(activity).inflate(R.layout.fragment_chat,container,false)
        mBinding = DataBindingUtil.bind(view)!!;
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }

}