package com.example.justtalk.Kotlin.UI

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.core.content.edit
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.lifecycle.ViewModelProvider
import com.example.justtalk.Kotlin.models.User
import com.example.justtalk.R
import com.example.justtalk.databinding.ActivityAuthBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kotlin.collections.HashMap
import kotlin.reflect.KClass

//const is added to global variables and can hold values that can be realized at compile time
//those values are immutable whearas val can do both
private const val TAG = "AuthActivity"
const val LOGGED_IN = "Login"
const val AUTH_SHARED_PREFERENCE="AuthPref"
const val UID="uid"
const val EMAIL="email"
const val PASS="pass"

class AuthActivity : AppCompatActivity(){
    /* Get the current fragment and if login then quit activity
    * LOGIN SHOULD ALWAYS BE THE FIRST */
    override fun onBackPressed() {
        val fragment = mBinding.container.getFragment<Fragment>()
        when(fragment){
            is LoginFragment ->   finish()
            else -> supportFragmentManager.popBackStack()
        }
    }

    lateinit private var mReference: DatabaseReference
    lateinit private var mAuth: FirebaseAuth

    lateinit private var mBinding : ActivityAuthBinding
    lateinit private var viewModel: AuthViewModel
    lateinit private var mUser: User

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        intent?.let{
            Log.e(TAG,"madar")
            val bundle = it.extras
            bundle?.let {
                val clrflag = it.getInt("clr")
                if(clrflag==1)
                {
                    Log.e(TAG,"madar2")
                    getSharedPreferences(AUTH_SHARED_PREFERENCE, MODE_PRIVATE).edit{
                        putBoolean(LOGGED_IN,false)
                    }
                }
            }
        }
        mBinding = DataBindingUtil.setContentView(this,R.layout.activity_auth)
        viewModel = ViewModelProvider(this).get(AuthViewModel::class.java)
        mReference = Firebase.database.reference.child("Users")
        val flag = loginCheck()
        if(flag)
            return
        mAuth = Firebase.auth
        mAuth.signOut()

    //Load LoginFragment only while the first start to the app
    if(savedInstanceState==null)
         makeTransaction(LoginFragment::class,null,"add")
    }

    //Logout when stop
    override fun onStop() {
        super.onStop()
        Log.e(TAG,"onStop called")
//        mAuth.signOut()
    }

    //Logout when destroyed
    override fun onDestroy() {
        super.onDestroy()
        Log.e(TAG,"onDestroy called")
//        mAuth.signOut()
    }

    //Global Function to either add or replace the fragment as desired
    fun <T:Fragment> makeTransaction(fragment: KClass<T>, bundle:Bundle?=null,option:String){
        when(option){
            "replace"-> {
                supportFragmentManager.commit {
                    replace(R.id.container, fragment.java, bundle)
                    addToBackStack(fragment.qualifiedName)
                }
            }
                "add" -> {
                        supportFragmentManager.commit {
                        add(R.id.container,fragment.java,bundle)
                        addToBackStack(fragment.qualifiedName)
                       }
                }
        }
    }

    //Tranfer the User object to mainActivity
    fun transferData(){
        val intent = Intent(this,MainActivity::class.java)
        val parcel = viewModel.mUser.value!!
        //parcel is a User object which actually extends the Serialiable and used in puExtra
        intent.putExtra("parcel",parcel)
        startActivity(intent)
        supportFragmentManager.fragments.let{
            if(it[it.size-1]::class.equals(SplashFragment::class))
                finish()
        }
    }

    /*Function to Find A record in the database with a given firebaseId and a transfer flag set to
    * transfer the contents to the mainActivity if set*/
    fun getUserAndUpdateVM(firebaseId:String,transferFlag:Boolean){
        /*orderByKey is a query to traverse the records via key*/
        mReference.orderByKey().addValueEventListener(object: ValueEventListener {

            //Called for each of the records with a snapshot of data at the location
            override fun onDataChange(snapshot: DataSnapshot){
                    /*GenericTypeIndicator is a wrapper for types that are generic in nature
                    * Here, It is passed to getValue as a instance of a object that extends GenericTypeIndicator*/
                    val result = snapshot.getValue(object:GenericTypeIndicator<HashMap<String,User>>(){})!!
                    result.values.forEach{
                        //For each record find record whose key matches with the one in the parameter firebaseId==this.uid
                        if((it.uid!!).equals(firebaseId)){
                            //Update this global variable and the one in the viewModel
                            mUser = it
                            viewModel.setUserValue(it)
                            //Transfer the User object to the MainActivity
                            if(transferFlag)
                            transferData()
                            //No need for furtherr traversing
                            return
                        }
                    }
            }
            override fun onCancelled(error: DatabaseError) {}
        })
    }
    fun loginCheck():Boolean{
        val sharedPref = getSharedPreferences(AUTH_SHARED_PREFERENCE, MODE_PRIVATE)
        val flag = sharedPref.getBoolean(LOGGED_IN,false)
        if(flag){
            makeTransaction(SplashFragment::class,null,"add")
            val email = sharedPref.getString(EMAIL,"err")!!
            val pass = sharedPref.getString(PASS,"err")!!
            Firebase.auth.signInWithEmailAndPassword(email,pass).addOnCompleteListener {
                if(it.isSuccessful){
                    getUserAndUpdateVM(it.result.user!!.uid,true)
                }else{
                    Toast.makeText(this, "Failure Logging in", Toast.LENGTH_SHORT).show()
                }
            }
            return true
        }else
            return false
    }
}