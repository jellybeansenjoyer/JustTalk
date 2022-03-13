package com.example.justtalk.Kotlin.UI

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.justtalk.Kotlin.models.Parcel
import com.example.justtalk.Kotlin.models.User
import com.example.justtalk.R
import com.example.justtalk.databinding.ActivityMainBinding
import com.google.android.material.tabs.TabLayout
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
        mViewModel = ViewModelProvider(this).get(MainActivityViewModel::class.java)
        if(savedInstanceState==null){
            mBinding.tabLayout.selectTab(mBinding.tabLayout.getTabAt(0))
            makeTransactions(ChatFragment::class)
        }
        mBinding.tabLayout.addOnTabSelectedListener(object: TabLayout.OnTabSelectedListener{

            override fun onTabSelected(tab: TabLayout.Tab?) {
                when(tab!!.text.toString()){
                    "CHATS"->{
                        makeTransactions(ChatFragment::class)
                    }
                    "FRIENDS"->{
                        makeTransactions(FriendsFragment::class)
                    }
                    "REQUESTS"->{
                        makeTransactions(FindFriendsFragment::class)
                    }
                }
            }
            override fun onTabUnselected(tab: TabLayout.Tab?) {
            }
            override fun onTabReselected(tab: TabLayout.Tab?) {
            }
        })
        val mIntent = intent
        val parcel = mIntent.getSerializableExtra("parcel") as User
        val fbuser = Firebase.auth.currentUser
        if(fbuser!=null){
            mViewModel.setUserValue(parcel)
        }
    }

    fun <T:Fragment> makeTransactions(fragment:KClass<T>,data:Bundle?=null){
        supportFragmentManager.commit {
            if(supportFragmentManager.fragments.size==0){
                add(R.id.container,fragment.java,data)
            }else{
                replace(R.id.container,fragment.java,data)
            }
            addToBackStack(fragment.qualifiedName)
        }
    }
}