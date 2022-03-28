package com.example.justtalk.test

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.GestureDetector
import android.view.MotionEvent
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
