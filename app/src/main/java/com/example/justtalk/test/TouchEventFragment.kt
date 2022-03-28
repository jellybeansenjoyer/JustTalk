package com.example.justtalk.test

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.core.view.MotionEventCompat
import androidx.databinding.DataBindingUtil
import com.example.justtalk.Kotlin.UI.MainActivity
import com.example.justtalk.R
import com.example.justtalk.databinding.FragmentTouchEventBinding

private const val TAG = "TouchEventFragment"
class TouchEventFragment : Fragment() {

    lateinit private var mBinding : FragmentTouchEventBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        mBinding = DataBindingUtil.inflate(inflater,R.layout.fragment_touch_event,container,false)
        container?.setOnTouchListener { view, motionEvent ->
            val mEvent = MotionEventCompat.getActionMasked(motionEvent)
            when(mEvent){
                MotionEvent.ACTION_DOWN->{
                    Log.e(TAG,"Action was Down")
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
                    false
                }
            }
        }
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }
}