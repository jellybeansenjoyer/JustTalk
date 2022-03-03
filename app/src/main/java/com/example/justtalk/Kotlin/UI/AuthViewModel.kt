package com.example.justtalk.Kotlin.UI

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.justtalk.Kotlin.models.User

class AuthViewModel : ViewModel() {
    private val _mData: MutableLiveData<String> = MutableLiveData()
    private val _mUser: MutableLiveData<User> = MutableLiveData()
            val mUser:LiveData<User> = _mUser
    val mData : LiveData<String> = _mData

    private val _mUsers:MutableLiveData<List<User>> = MutableLiveData()
    val mUsers:LiveData<List<User>>
    get() = _mUsers

    public fun setUserValue(user:User){
        _mUser.value = user
    }

    public fun setDate(date:String){
        _mData.value = date
    }

    public fun setList(users:List<User>){
        _mUsers.value = users
    }

}