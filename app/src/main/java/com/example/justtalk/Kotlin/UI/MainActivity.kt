package com.example.justtalk.Kotlin.UI

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowManager
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
import com.example.justtalk.test.AnimationTestFragment
import com.example.justtalk.test.FirebaseStorageTest
import com.example.justtalk.test.RecyclerViewRemoveIt
import com.google.android.material.tabs.TabLayout
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlin.reflect.KClass

private const val TAG = "MainActivity"
class MainActivity : AppCompatActivity() {

    lateinit var mBinding : ActivityMainBinding
    lateinit private var mViewModel : MainActivityViewModel
    override fun onBackPressed() {
        Log.e(TAG,supportFragmentManager.fragments.size.toString())
        if(supportFragmentManager.fragments.size==2){
            finish()
        }else{
            supportFragmentManager.popBackStack()
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //intialize view model
        val window = this.window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.statusBarColor = resources.getColor(R.color.marine_green)
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        makeTransactions(AnimationTestFragment::class)
        return
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
                        val bundle = Bundle()
                        bundle.putInt("FindFriendsId",1)
                        makeTransactions(FindFriendsFragment::class,bundle)
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