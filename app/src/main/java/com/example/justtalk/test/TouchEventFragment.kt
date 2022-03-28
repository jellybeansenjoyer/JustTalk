package com.example.justtalk.test

import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import androidx.core.view.GestureDetectorCompat
import androidx.core.view.MotionEventCompat
import androidx.databinding.DataBindingUtil
import com.example.justtalk.Kotlin.UI.MainActivity
import com.example.justtalk.R
import com.example.justtalk.databinding.FragmentTouchEventBinding

private const val TAG = "TouchEventFragment"
class TouchEventFragment : Fragment() {

    lateinit private var mBinding : FragmentTouchEventBinding
    lateinit private var mGesture : GestureDetectorCompat
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        mBinding = DataBindingUtil.inflate(inflater,R.layout.fragment_touch_event,container,false)

        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

         mGesture = GestureDetectorCompat(this.context,object:GestureDetector.OnGestureListener{
            override fun onDown(p0: MotionEvent?): Boolean {
                Log.e(TAG,"onDown Called")
                return true
            }

            override fun onShowPress(p0: MotionEvent?) {
                Log.e(TAG,"onShowPress Called")
            }

            override fun onSingleTapUp(p0: MotionEvent?): Boolean {
                Log.e(TAG,"onSingleTapUp Called")
                return true
            }

            override fun onScroll(
                p0: MotionEvent?,
                p1: MotionEvent?,
                p2: Float,
                p3: Float
            ): Boolean {
                Log.e(TAG,"onScroll called")
                return true
            }

            override fun onLongPress(p0: MotionEvent?) {
                Log.e(TAG,"onLongPress Called")
            }

            override fun onFling(
                p0: MotionEvent?,
                p1: MotionEvent?,
                p2: Float,
                p3: Float
            ): Boolean {
                Log.e(TAG,"onFling Called")
                return true
            }

        }).apply {
             setOnDoubleTapListener(object : GestureDetector.OnDoubleTapListener {
                 override fun onSingleTapConfirmed(p0: MotionEvent?): Boolean {
                     Log.e(TAG, "onSingleTapConfirmed Called")
                     return true
                 }

                 override fun onDoubleTap(p0: MotionEvent?): Boolean {
                     Log.e(TAG, "onDoubleTap Called")
                     return true
                 }

                 override fun onDoubleTapEvent(p0: MotionEvent?): Boolean {
                     Log.e(TAG, "onDoubleTapEvent Called")
                     return true
                 }

             })

             view.setOnTouchListener { view, motionEvent ->
                  mGesture.onTouchEvent(motionEvent)
             }
         }
    }
}