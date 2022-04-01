package com.example.justtalk.Kotlin.UI

import android.gesture.Gesture
import android.icu.number.NumberFormatter
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.method.Touch.onTouchEvent
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.VelocityTracker
import android.widget.Button
import androidx.core.view.GestureDetectorCompat
import androidx.dynamicanimation.animation.DynamicAnimation
import androidx.dynamicanimation.animation.FlingAnimation
import com.example.justtalk.R

class FlingTestActivity : AppCompatActivity() {
    lateinit private var mFling: FlingAnimation
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_fling_test)
        findViewById<Button>(R.id.fling).apply{
            val fling = FlingAnimation(this,DynamicAnimation.SCROLL_X)
            setOnTouchListener { view, motionEvent ->
                VelocityTracker.obtain().apply {
                    addMovement(motionEvent)
                    computeCurrentVelocity(1)
                    fling.setStartVelocity(xVelocity).start()
                }
                true }
        }
    }

}