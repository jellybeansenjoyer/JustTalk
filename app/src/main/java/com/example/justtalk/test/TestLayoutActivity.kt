package com.example.justtalk.test

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import com.example.justtalk.R
import com.google.android.material.tabs.TabLayout

private const val TAG = "TestLayoutActivity"
class TestLayoutActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.layout_test_tabs)
        val tabLayout = findViewById<TabLayout>(R.id.tabLayout)
        makeTransaction(BlankFragment1())
        tabLayout.addOnTabSelectedListener(object:TabLayout.OnTabSelectedListener{
            override fun onTabSelected(tab: TabLayout.Tab?) {
                when(tab!!.text.toString()){
                    "CHATS"->{
                        makeTransaction(BlankFragment1())
                    }
                    "FRIENDS"->{
                        makeTransaction(BlankFragment2())
                    }
                    "REQUESTS"->{
                        makeTransaction(BlankFragment3())
                    }
                }
            }
            override fun onTabUnselected(tab: TabLayout.Tab?) {
            }
            override fun onTabReselected(tab: TabLayout.Tab?) {
            }
        })
    }
    fun makeTransaction(fragment: Fragment){
            supportFragmentManager.commit {
                if(supportFragmentManager.fragments.size==0)
                    add(R.id.container,fragment)
                else
                    replace(R.id.container,fragment)
                addToBackStack(fragment::class.toString())
            }
    }
}