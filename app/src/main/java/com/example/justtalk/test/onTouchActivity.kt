package com.example.justtalk.test

import android.animation.ObjectAnimator
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.animation.doOnEnd
import androidx.core.view.GestureDetectorCompat
import androidx.core.view.MotionEventCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.commit
import com.example.justtalk.R
import com.example.justtalk.databinding.ActivityOnTouchBinding

private const val TAG = "onTouchActivity"
class onTouchActivity : AppCompatActivity() {
    lateinit private var mBinding: ActivityOnTouchBinding
    lateinit private var mGesture: GestureDetectorCompat
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_on_touch)
        val def_duration = resources.getInteger(android.R.integer.config_mediumAnimTime)
        val f1:ObjectAnimator
        val f2:ObjectAnimator
        mBinding.content.apply{
            //Visible But transparent Earlier : Was Gone
            alpha = 0f
            visibility = View.VISIBLE

             f1 = ObjectAnimator.ofFloat(this,View.ALPHA,1f).apply{
                    duration = def_duration.toLong()
            }

        }
        mBinding.testButton.apply{
             //Was Earlier VISIBLE now we want it to slowly fade and when completely 0 , is completely gone
             f2 = ObjectAnimator.ofFloat(this,View.ALPHA,0f).apply{
                duration = def_duration.toLong()
            }.apply {
                 doOnEnd {
                     visibility = View.GONE
                 }
             }
        }
        mBinding.testButton.setOnClickListener {
            f1.start()
            f2.start()
        }
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        event?.apply{
            mBinding.testButton.x = x
            mBinding.testButton.y = y
        }
        return super.onTouchEvent(event)
    }
}


//
//    override fun onTouchEvent(event: MotionEvent?): Boolean {
//        event?.apply{
//            val pointerSize = pointerCount
//            Log.e(TAG+"historysize",historySize.toString())
//            for(p0 in 0..pointerSize-1){
//                val p = getPointerId(p0)
//                val x = getX(p)
//                val y = getY(p)
//                Log.e(TAG,"P:${p} X:${x} Y:${y} ${x==this.x}")
//            }
//        }
//        return super.onTouchEvent(event)
//    }
//}
//
//        mGesture = GestureDetectorCompat(this,object: GestureDetector.OnGestureListener{
//            override fun onDown(p0: MotionEvent?): Boolean {
//                Log.e(TAG,"onDown Called")
//                return true
//            }
//
//            override fun onShowPress(p0: MotionEvent?) {
//                Log.e(TAG,"onShowPress Called")
//            }
//
//            override fun onSingleTapUp(p0: MotionEvent?): Boolean {
//                Log.e(TAG,"onSingleTapUp Called")
//                return true
//            }
//
//            override fun onScroll(
//                p0: MotionEvent?,
//                p1: MotionEvent?,
//                p2: Float,
//                p3: Float
//            ): Boolean {
//                Log.e(TAG,"onScroll called X:${p2} Y:${p3} ")
//                return true
//            }
//
//            override fun onLongPress(p0: MotionEvent?) {
//                Log.e(TAG,"onLongPress Called")
//            }
//
//            override fun onFling(
//                p0: MotionEvent?,
//                p1: MotionEvent?,
//                p2: Float,
//                p3: Float
//            ): Boolean {
//                Log.e(TAG,"onFling Called Vx:${p2} Vy:${p3} ")
//                return true
//            }
//
//        }).apply {
//            setOnDoubleTapListener(object : GestureDetector.OnDoubleTapListener {
//                override fun onSingleTapConfirmed(p0: MotionEvent?): Boolean {
//                    Log.e(TAG, "onSingleTapConfirmed Called")
//                    return true
//                }
//
//                override fun onDoubleTap(p0: MotionEvent?): Boolean {
//                    Log.e(TAG, "onDoubleTap Called")
//                    return true
//                }
//
//                override fun onDoubleTapEvent(p0: MotionEvent?): Boolean {
//                    Log.e(TAG, "onDoubleTapEvent Called")
//                    return true
//                }
//
//            })
//
//        }
//
//    }
//
//    override fun onTouchEvent(event: MotionEvent?): Boolean {
//
//        val size = event!!.historySize
//        return if(mGesture.onTouchEvent(event)){
//            true
//        }else{
//            super.onTouchEvent(event)
//        }
//    }
//    override fun onTouchEvent(event: MotionEvent?): Boolean {
//        val mEvent = MotionEventCompat.getActionMasked(event)
//        return when(mEvent){
//            MotionEvent.ACTION_DOWN->{
//                Log.e(TAG,"Action was Down")
//                true
//            }
//            MotionEvent.ACTION_CANCEL->{
//                Log.e(TAG,"Action was Cancel")
//                true
//            }
//            MotionEvent.ACTION_UP->{
//                Log.e(TAG,"Action was Up")
//                true
//            }
//            MotionEvent.ACTION_MOVE->{
//                Log.e(TAG,"Action was Move")
//                true
//            }
//            else->{
//                super.onTouchEvent(event)
//            }
//        }
//
//    }
