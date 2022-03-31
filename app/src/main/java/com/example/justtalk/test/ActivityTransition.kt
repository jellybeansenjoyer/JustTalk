package com.example.justtalk.test

import android.animation.*
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.core.view.ViewCompat.animate
import androidx.fragment.app.commit
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
                enterExitAnimation()
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