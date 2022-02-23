package com.example.justtalk.Kotlin.UI

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import com.example.justtalk.R
import com.example.justtalk.databinding.ActivityMainBinding
import kotlin.reflect.KClass

class MainActivity : AppCompatActivity() {

    lateinit private var binding : ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

    }
    fun <T:Fragment> makeTransactions(fragment: KClass<T>,data:Bundle?=null){
        supportFragmentManager.commit {
            add(R.id.container,fragment.java,data)
            addToBackStack("ChatFragment")
        }
    }
}