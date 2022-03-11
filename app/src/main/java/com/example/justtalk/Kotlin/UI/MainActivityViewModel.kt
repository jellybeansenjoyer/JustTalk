package com.example.justtalk.Kotlin.UI

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.justtalk.Kotlin.models.ChatRef
import com.example.justtalk.Kotlin.models.Message
import com.example.justtalk.Kotlin.models.User
import com.google.firebase.auth.FirebaseUser

class MainActivityViewModel : ViewModel() {

    private val _mUser: MutableLiveData<User> = MutableLiveData()
    val mUser : LiveData<User> = _mUser

    private val _mSSId : MutableLiveData<String> = MutableLiveData()
    val mSSId:LiveData<String?> = _mSSId

    private var _listOfRequests : MutableLiveData<ArrayList<User>> = MutableLiveData()
    val listOfRequests:LiveData<ArrayList<User>>
    get()=_listOfRequests

    private var _listOfFriends : MutableLiveData<ArrayList<User>> = MutableLiveData()
    val listOfFriends: LiveData<ArrayList<User>>
    get() = _listOfFriends

    private var currentRoom : MutableLiveData<ChatRef> = MutableLiveData()
    val _currentRoom : LiveData<ChatRef>
    get() = currentRoom

    private var mMessageList : MutableLiveData<ArrayList<Message>> = MutableLiveData()
    val _messageList : LiveData<ArrayList<Message>>
    get() = mMessageList

    fun setMessageList(messageList:ArrayList<Message>){
        mMessageList.value = messageList
    }
    fun setCurrentRoom(room:ChatRef){
        currentRoom.value = room
    }
    fun setListOfFriends(list:ArrayList<User>){
        _listOfFriends.value = list
    }

    fun setListOfRequests(list:ArrayList<User>){
        _listOfRequests.value = list
    }

    fun setUserValue(user:User){
        _mUser.value = user
    }

    fun setSSIDValue(ssid:String){
        _mSSId.value = ssid
    }

}