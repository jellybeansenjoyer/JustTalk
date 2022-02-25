package com.example.justtalk.Kotlin.UI

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class AuthViewModel : ViewModel() {
    private val _mDate: MutableLiveData<String> = MutableLiveData()
    val mData : LiveData<String> = _mDate

    public fun setDate(date:String){
        _mDate.value = date
    }
}