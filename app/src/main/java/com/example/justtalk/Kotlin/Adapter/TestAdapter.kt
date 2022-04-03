package com.example.justtalk.Kotlin.Adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.justtalk.R
import com.example.justtalk.databinding.TestModelBinding

private const val TAG = "TestAdapter"
class TestAdapter() : RecyclerView.Adapter<TestAdapter.TestViewHolder>() {
    private var oldList:ArrayList<String> =  ArrayList()

     class TestViewHolder(private val mView : View) : RecyclerView.ViewHolder(mView){
         lateinit private var mBinding : TestModelBinding
         init{
             mBinding = DataBindingUtil.bind(mView)!!
         }
        fun bind(word:String){
            Log.e(TAG,"bind called")
            mBinding.textTest.setText(word)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TestViewHolder {
        //Instance of the Inner Class To be Created to Access the bind Object Via the onBindViewHolder
        Log.e(TAG,"onCreateViewHolder called")
        val view = LayoutInflater.from(parent.context).inflate(R.layout.test_model,parent,false)
        return TestViewHolder(view)
    }

    override fun onBindViewHolder(holder: TestViewHolder, position: Int) {
        //Accessing the Members of the ViewHolder inner class
        Log.e(TAG,"onBindViewHolderCalled")
        holder.bind(oldList[position])
    }

    override fun getItemCount(): Int {
        //Size of the list
        return oldList.size
    }

    fun submitlist(newList : ArrayList<String>){
        val diffutil = object : DiffUtil.Callback(){
            override fun getOldListSize() = oldList.size

            override fun getNewListSize() = newList.size
            override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
                 return oldList.get(oldItemPosition).equals(newList.get(newItemPosition))
            }

            override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
                return oldList.get(oldItemPosition).equals(newList.get(newItemPosition))
            }
        }
        val calc = DiffUtil.calculateDiff(diffutil)
        oldList = newList
        Log.e(TAG,oldList.size.toString())
        oldList.forEach{
            Log.e(TAG+"3",it)
        }
        calc.dispatchUpdatesTo(this)
        this.notifyItemInserted(oldList.size-1)
    }
    fun appendWord(word: String){
        oldList.add(word)
        this.notifyItemInserted(oldList.size-1)
    }
}