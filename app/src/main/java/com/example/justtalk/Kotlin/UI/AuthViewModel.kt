package com.example.justtalk.Kotlin.UI

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.justtalk.Kotlin.models.Parcel
import com.example.justtalk.Kotlin.models.User

class AuthViewModel : ViewModel() {
    private val _mData: MutableLiveData<String> = MutableLiveData()
    val mData : LiveData<String> = _mData

    private val _mUser: MutableLiveData<User> = MutableLiveData()
            val mUser:LiveData<User> = _mUser

    private val _mUsers:MutableLiveData<List<User>> = MutableLiveData()
    val mUsers:LiveData<List<User>>
    get() = _mUsers

    private val _userKey: MutableLiveData<String> = MutableLiveData()
    val userKey: LiveData<String>
    get() = _userKey

    private val _ssid : MutableLiveData<String> = MutableLiveData()
    val ssid :  LiveData<String>
    get() = _ssid



    public fun setUserKey(key:String){
        _userKey.value = key
    }
    public fun setUserValue(user:User){
        _mUser.value = user
    }

    public fun setDate(date:String){
        _mData.value = date
    }

    public fun setList(users:List<User>){
        _mUsers.value = users
    }
    public fun setSSID(ssid:String){
        _ssid.value = ssid
        _mUser.value!!.chatroomref = _ssid.value!!
    }
    fun exportParcel():Parcel{
        return Parcel(mUser.value!!,ssid.value)
    }
}