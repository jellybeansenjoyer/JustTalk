package com.example.justtalk.test

import android.animation.*
import android.graphics.Path
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.GestureDetector
import android.view.View
import android.view.ViewAnimationUtils
import android.view.animation.AccelerateDecelerateInterpolator
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.core.animation.doOnEnd
import androidx.core.view.ViewCompat.animate
import androidx.dynamicanimation.animation.DynamicAnimation
import androidx.dynamicanimation.animation.FlingAnimation
import androidx.fragment.app.commit
import androidx.fragment.app.replace
import com.example.justtalk.R

class ActivityTransition : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_transition)
        if(savedInstanceState==null){
            supportFragmentManager.commit {
                add(R.id.container,BlankFragment_1())
            }
        }

        findViewById<Button>(R.id.button_press).apply{
            setOnClickListener {

            }
        }
    }
    fun addPath(){
        Path().addArc(0f, 0f, 1000f, 1000f, 270f, -180f)
    }
    fun circularAnimation(){
        findViewById<ImageView>(R.id.photo).apply{
            val v = this
            val cx = width/2
            val cy = height/2
            val hypot = Math.hypot(cx.toDouble(),cy.toDouble()).toFloat()
            ViewAnimationUtils.createCircularReveal(this,cx,cy,0f,hypot).apply{
                interpolator = AccelerateDecelerateInterpolator()
                duration = 2000
                start()
                v.visibility = View.VISIBLE
            }

        }
    }
    fun enterExitAnimation(){
        supportFragmentManager.commit {
            setCustomAnimations(R.animator.card_flip_right_in,R.animator.card_flip_right_out,R.animator.card_flip_left_in,R.animator.card_flip_left_out )
            replace(R.id.container,BlankFragment_2())
            addToBackStack(null)
        }
    }
}