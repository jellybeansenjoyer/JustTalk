package com.example.justtalk.test

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import androidx.core.view.MotionEventCompat
import androidx.databinding.DataBindingUtil
import com.example.justtalk.R
import com.example.justtalk.databinding.ActivityOnTouchBinding

private const val TAG = "onTouchActivity"
class onTouchActivity : AppCompatActivity() {
    lateinit private var mBinding : ActivityOnTouchBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_on_touch)

    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        val mEvent = MotionEventCompat.getActionMasked(event)
        return when(mEvent){
            MotionEvent.ACTION_DOWN->{
                Log.e(TAG,"Action was Down")
                true
            }
            MotionEvent.ACTION_CANCEL->{
                Log.e(TAG,"Action was Cancel")
                true
            }
            MotionEvent.ACTION_UP->{
                Log.e(TAG,"Action was Up")
                true
            }
            MotionEvent.ACTION_MOVE->{
                Log.e(TAG,"Action was Move")
                true
            }
            else->{
                super.onTouchEvent(event)
            }
        }

    }
}