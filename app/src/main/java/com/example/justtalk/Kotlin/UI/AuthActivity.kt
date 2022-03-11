package com.example.justtalk.Kotlin.UI

import android.app.DatePickerDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.DatePicker
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.lifecycle.ViewModelProvider
import com.example.justtalk.Kotlin.models.User
import com.example.justtalk.R
import com.example.justtalk.databinding.ActivityAuthBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import java.text.DateFormat
import java.util.*
import kotlin.collections.HashMap
import kotlin.reflect.KClass

private const val TAG = "AuthActivity"

class AuthActivity : AppCompatActivity(),
    DatePickerDialog.OnDateSetListener {

    lateinit private var mReference: DatabaseReference
    lateinit private var mBinding : ActivityAuthBinding
    lateinit private var fUser: FirebaseUser
    lateinit private var viewModel: AuthViewModel
    lateinit private var mUser: User
    lateinit private var mAuth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = DataBindingUtil.setContentView(this,R.layout.activity_auth)
        viewModel = ViewModelProvider(this).get(AuthViewModel::class.java)
        mReference = Firebase.database.reference.child("Users")
        mAuth = Firebase.auth
        var fUserId:String? = null
        mAuth.signOut()
        if(mAuth.currentUser!=null){
            fUser = mAuth.currentUser!!
            fUserId = fUser.uid
            getUserAndUpdateVM(mAuth.currentUser!!.uid,true)
        }

    if(savedInstanceState==null)
         makeTransaction(LoginFragment::class,null,"add")
    }

    override fun onStop() {
        super.onStop()
        mAuth.signOut()
    }

    override fun onDestroy() {
        super.onDestroy()
        mAuth.signOut()
    }

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

    fun showDialog(){
        DatePickerFragment(this).show(supportFragmentManager,"DatePickerDialog")
    }

    //transfer data to the main activity
    fun transferData(){
        val intent = Intent(this,MainActivity::class.java)
        val parcel = viewModel.mUser.value!!
        intent.putExtra("parcel",parcel)
        startActivity(intent)
    }

    fun getUserAndUpdateVM(firebaseId:String,transferFlag:Boolean){
        mReference.orderByKey().addValueEventListener(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot){
                    val result = snapshot.getValue(object:GenericTypeIndicator<HashMap<String,User>>(){})!!
                    result.values.forEach{
                        if((it.uid!!).equals(firebaseId)){
                            mUser = it
                            viewModel.setUserValue(it)
                            if(transferFlag)
                            transferData()
                            return
                        }
                    }
            }
            override fun onCancelled(error: DatabaseError) {
            }
        })
        Log.e(TAG,"ended")
    }

    override fun onDateSet(p0: DatePicker?, p1: Int, p2: Int, p3: Int) {
        val c = Calendar.getInstance()
        c.set(Calendar.YEAR,p1)
        c.set(Calendar.MONTH,p2)
        c.set(Calendar.DAY_OF_MONTH,p3)
        val currentDate = DateFormat.getDateInstance().format(c.time)
        viewModel.setDate(currentDate)
    }
}