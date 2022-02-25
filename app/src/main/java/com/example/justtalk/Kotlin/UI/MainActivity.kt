package com.example.justtalk.Kotlin.UI

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.justtalk.Kotlin.models.User
import com.example.justtalk.R
import com.example.justtalk.databinding.ActivityMainBinding
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlin.reflect.KClass

class MainActivity : AppCompatActivity() {

    lateinit private var mBinding : ActivityMainBinding
    lateinit private var mViewModel : MainActivityViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //intialize view model


        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        val mIntent = intent
        val user = mIntent.getSerializableExtra("AuthActivity") as User

        val fbuser = Firebase.auth.currentUser

        if(fbuser!=null){
            mViewModel.setUserValue(user)
            makeTransactions(ChatFragment::class)
        }
    }

    fun <T:Fragment> makeTransactions(fragment:KClass<T>,data:Bundle?=null){
        supportFragmentManager.commit {
            add(R.id.container,fragment.java,data)
            addToBackStack(fragment.java.name)
        }
    }
}