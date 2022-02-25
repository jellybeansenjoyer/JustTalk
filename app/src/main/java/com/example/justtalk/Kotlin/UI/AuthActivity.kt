package com.example.justtalk.Kotlin.UI

import android.app.DatePickerDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.DatePicker
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import com.example.justtalk.Kotlin.models.User
import com.example.justtalk.R
import com.example.justtalk.databinding.ActivityAuthBinding
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import java.text.DateFormat
import java.util.*
import kotlin.reflect.KClass

class AuthActivity : AppCompatActivity(),DatePickerDialog.OnDateSetListener {
    lateinit private var mReference: DatabaseReference
    lateinit private var mBinding : ActivityAuthBinding
    lateinit private var fUser: FirebaseUser
    lateinit private var viewModel: AuthViewModel
    private var mUser: User? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = DataBindingUtil.setContentView(this,R.layout.activity_auth)
        mReference = Firebase.database.reference.child("Users")

        val auth = Firebase.auth

        var fUserId:String? = null
        if(auth.currentUser!=null){
            fUser = auth.currentUser!!
            fUserId = fUser.uid
            getUser(fUserId)
        }else{
            //Make authentication
            makeTransaction(LoginFragment::class)
        }



    }
    fun <T:Fragment> makeTransaction(fragment: KClass<T>, bundle:Bundle?=null){
        supportFragmentManager.commit {
            add(R.id.container,fragment.java,bundle)
            addToBackStack(fragment::class.qualifiedName)
        }
    }
    fun showDialog(){
        DatePickerFragment(this).show(supportFragmentManager,"DatePickerDialog")
    }
    fun transferData(){
        val intent = Intent()
        intent.putExtra("AuthActivity",mUser)
        startActivityForResult(intent,123)
    }
    fun getUser(fUserId:String){
        mReference.orderByChild("Id").equalTo(fUserId).addValueEventListener(object:
            ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                snapshot.children.forEach {
                    val result = it.getValue(User::class.java)
                    mUser = result
                }
            }
            override fun onCancelled(error: DatabaseError) {
            }
        })
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