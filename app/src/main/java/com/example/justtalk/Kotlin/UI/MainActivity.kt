package com.example.justtalk.Kotlin.UI

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
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
import com.example.justtalk.test.FirebaseStorageTest
import com.example.justtalk.test.RecyclerViewRemoveIt
import com.google.android.material.tabs.TabLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlin.reflect.KClass

private const val TAG = "MainActivity"
class MainActivity : AppCompatActivity() {
    lateinit private var mAuth:FirebaseAuth
    lateinit var mBinding : ActivityMainBinding
    lateinit private var mViewModel : MainActivityViewModel
    lateinit private var mAuthViewModel: AuthViewModel
    override fun onBackPressed() {
        Log.e(TAG,supportFragmentManager.fragments.size.toString())
        if(supportFragmentManager.fragments.size==1){
            finish()
        }else{
            supportFragmentManager.popBackStack()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
         menuInflater.inflate(R.menu.toolbar_menu,menu)
           return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.userInfo -> {
                mBinding.toolbar.visibility = View.GONE
                mBinding.tabLayout.visibility = View.GONE
                setStatusBarColor(R.color.white)
                makeTransactions(FragmentUserProfile::class)
                return true
            }
            R.id.logOut -> {
                mAuth.signOut()
                val intent = Intent(this, AuthActivity::class.java)
                intent.putExtra("clr",1)
                startActivity(intent)
                mAuthViewModel.reset()
                this.finish()
                return true
            }
            else -> {
                return super.onOptionsItemSelected(item)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //intialize view model
        Log.e(TAG,"came here")
        setStatusBarColor(R.color.marine_green)

        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        mViewModel = ViewModelProvider(this).get(MainActivityViewModel::class.java)
        mAuthViewModel = ViewModelProvider(this).get(AuthViewModel::class.java)

        setSupportActionBar(mBinding.toolbar)
        mBinding.toolbar.visibility = View.VISIBLE

        supportFragmentManager.apply{
            if(fragments.size>0){
                if(fragments[0] is TalkFragment){
                    mBinding.tabLayout.visibility = View.GONE
                    mBinding.toolbar.visibility = View.GONE
                }
            }
        }
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
        mAuth = Firebase.auth
//        mAuth.addAuthStateListener(object : FirebaseAuth.AuthStateListener{
//            override fun onAuthStateChanged(p0: FirebaseAuth) {
//                if(p0==null){
//                    Log.e(TAG,"NULL VALUE")
//                }else{
//                    Log.e(TAG,"NOT NULL VALUE")
//                }
//            }
//        })
        val fbuser = mAuth.currentUser!!
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
            Log.e(TAG,supportFragmentManager.fragments.size.toString())
        }
    }
    fun setStatusBarColor(color:Int){
        val window = this.window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.statusBarColor = resources.getColor(color)
    }

    override fun onStop() {
        super.onStop()
        Log.e(TAG,"The MainActivity is stopped")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.e(TAG,"The MainActivity is dead")
    }

}