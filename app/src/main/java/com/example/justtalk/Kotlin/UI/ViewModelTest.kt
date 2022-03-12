package com.example.justtalk.Kotlin.UI

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ViewModelTest : ViewModel() {

    private val listOfWords : MutableLiveData<ArrayList<String>> = MutableLiveData()
    val _listOfWords : LiveData<ArrayList<String>>
    get() = listOfWords

    fun setListOfWords(list:ArrayList<String>){
        listOfWords.value = list
    }
    fun setListModified(word: String){
        val list = listOfWords.value!!
        list.add(word)
        listOfWords.value = list
    }
    init {
        listOfWords.value = ArrayList()
    }
}