package com.example.justtalk.Kotlin.UI

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.justtalk.Kotlin.models.User
import com.google.firebase.auth.FirebaseUser

class MainActivityViewModel : ViewModel() {
    private val _mUser: MutableLiveData<User> = MutableLiveData()
    val mUser : LiveData<User> = _mUser

    private val _mFbUser:MutableLiveData<FirebaseUser> = MutableLiveData()
    val mFbUser : LiveData<FirebaseUser> = _mFbUser

    fun setUserValue(user:User){
        _mUser.value = user
    }

    fun setFbUserValue(fbuser:FirebaseUser){
        _mFbUser.value = fbuser
    }
}