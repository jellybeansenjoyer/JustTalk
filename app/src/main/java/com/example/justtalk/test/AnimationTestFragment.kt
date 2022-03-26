package com.example.justtalk.test

import android.animation.ObjectAnimator
import android.animation.PropertyValuesHolder
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AlphaAnimation
import androidx.databinding.DataBindingUtil
import com.example.justtalk.R
import com.example.justtalk.databinding.FragmentAnimationTestBinding

class AnimationTestFragment : Fragment() {
    lateinit private var mBinding : FragmentAnimationTestBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        mBinding = DataBindingUtil.inflate(inflater,R.layout.fragment_animation_test,container,false)
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val alphaAnim = ObjectAnimator.ofFloat(mBinding.alpha,View.ALPHA,0f).apply {
            duration = 1000
            repeatCount = 1
            repeatMode = ObjectAnimator.REVERSE
        }

        val transAnim = ObjectAnimator.ofFloat(mBinding.trans,View.TRANSLATION_X,800f).apply {
            duration = 1000
            repeatCount = 1
            repeatMode = ObjectAnimator.REVERSE
        }

        val rotatAnim = ObjectAnimator.ofFloat(mBinding.rotate,View.ROTATION,360f).apply {
            duration = 1000
            repeatCount = 1
            repeatMode = ObjectAnimator.REVERSE
        }

        mBinding.alpha.setOnClickListener {
            alphaAnim.start()
        }
        mBinding.trans.setOnClickListener {
            transAnim.start()
        }
        mBinding.rotate.setOnClickListener {
            rotatAnim.start()
        }
    }
}