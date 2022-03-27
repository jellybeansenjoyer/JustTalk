package com.example.justtalk.test

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.animation.PropertyValuesHolder
import android.animation.ValueAnimator
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AlphaAnimation
import androidx.databinding.DataBindingUtil
import com.example.justtalk.R
import com.example.justtalk.databinding.FragmentAnimationTestBinding
import java.time.temporal.ValueRange

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
        mBinding.set.setOnClickListener {
            when{
                mBinding.checkBox.isChecked ->{
                    objectAnim()
                }
                else->{
                    valueAnim()
                }
            }
        }

    }

    fun valueAnim(){
        val alphaAnim = ValueAnimator.ofFloat(mBinding.alpha.alpha,0f).apply {
            duration = 500
            addUpdateListener {
                mBinding.alpha.alpha = it.animatedValue as Float
            }
            repeatCount = 1
            repeatMode = ValueAnimator.REVERSE
        }

        val transAnim = ValueAnimator.ofFloat(mBinding.trans.translationX,800f).apply {
            duration = 200
            addUpdateListener {
                mBinding.trans.translationX = it.animatedValue as Float
            }
            repeatCount = 1
            repeatMode = ValueAnimator.REVERSE
        }

        val rotateAnim = ValueAnimator.ofFloat(mBinding.rotate.rotation,360f).apply{
            duration = 200
            addUpdateListener {
                mBinding.rotate.rotation = it.animatedValue as Float
            }
            repeatCount = 1
            repeatMode = ValueAnimator.REVERSE
        }
        mBinding.trans.setOnClickListener {
            transAnim.start()
        }
        mBinding.alpha.setOnClickListener {
            alphaAnim.start()
        }
        mBinding.rotate.setOnClickListener {
            rotateAnim.start()
        }
    }
    fun objectAnim(){
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