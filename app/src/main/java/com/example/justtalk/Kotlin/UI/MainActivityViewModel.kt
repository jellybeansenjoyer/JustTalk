package com.example.justtalk.Kotlin.UI

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.justtalk.Kotlin.models.*
import com.google.firebase.auth.FirebaseUser

private const val TAG = "MainActivityViewModel"
class MainActivityViewModel : ViewModel() {

    private val _mUser: MutableLiveData<User> = MutableLiveData()
    val mUser : LiveData<User> = _mUser

    private val _mSSId : MutableLiveData<String> = MutableLiveData()
    val mSSId:LiveData<String?> = _mSSId

    private var _listOfRequests : MutableLiveData<ArrayList<User>> = MutableLiveData()
    val listOfRequests:LiveData<ArrayList<User>>
    get()=_listOfRequests

    private var _listOfFriends : MutableLiveData<ArrayList<ChatFrag>> = MutableLiveData()
    val listOfFriends: LiveData<ArrayList<ChatFrag>>
    get() = _listOfFriends

    private var mListOfIndivisuals: MutableLiveData<ArrayList<User>> = MutableLiveData()
    val listOfIndivisuals: LiveData<ArrayList<User>>
        get() = mListOfIndivisuals

    private var currentRoom : MutableLiveData<ChatRef> = MutableLiveData()
    val _currentRoom : LiveData<ChatRef>
    get() = currentRoom

    private var mMessageList : MutableLiveData<ArrayList<Message>> = MutableLiveData()
    val _messageList : LiveData<ArrayList<Message>>
    get() = mMessageList

    private var mFriend : MutableLiveData<User> = MutableLiveData()
    val _mFriend : LiveData<User>
    get() = mFriend

    private var mListOfFriendsGrp: MutableLiveData<List<User>> = MutableLiveData()
    val _mListOfFriendsGrp:LiveData<List<User>>
    get() = mListOfFriendsGrp

    private var mGroup: MutableLiveData<Group> = MutableLiveData()
    val _mGroup: LiveData<Group>
    get() = mGroup

    private var mGroupRoom:MutableLiveData<String> = MutableLiveData()
    val _mGroupRoom:LiveData<String>
    get() = mGroupRoom

    init {
        mMessageList.value = ArrayList<Message>()
    }
    fun setGrp(grp:Group){
        mGroup.value = grp
    }
    fun setGrpFriendsList(list:List<User>){
        mListOfFriendsGrp.value = list
    }
    fun setFriend(friend : User){
        mFriend.value = friend
    }

    fun setMessageList(messageList:ArrayList<Message>){
        mMessageList.value = messageList
    }

    fun setMessageModified(message:Message){
        mMessageList.value!!.add(message)
        mMessageList.postValue(mMessageList.value)
    }
    fun setCurrentRoom(room:ChatRef){
        currentRoom.value = room
    }
    fun setListOfFriends(list:ArrayList<ChatFrag>){
        _listOfFriends.value = list
        Log.e(TAG,"list of friends called+${list.size} ")
    }

    fun setListOfRequests(list:ArrayList<User>){
        _listOfRequests.value = list
    }

    fun setListOfIndivisuals(list:ArrayList<User>){
        mListOfIndivisuals.value = list
    }
    fun setUserValue(user:User){
        _mUser.value = user
    }
    fun setGrpRoomString(grp:String){
        mGroupRoom.value = grp
    }
    fun setSSIDValue(ssid:String){
        _mSSId.value = ssid
    }

}