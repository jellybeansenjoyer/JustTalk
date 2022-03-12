package com.example.justtalk.Kotlin.Adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.justtalk.R

private const val TAG = "SimpleAdapter"
class SimpleAdapter : RecyclerView.Adapter<SimpleAdapter.RviewHolder>(){
    private var oldList = ArrayList<String>()
    class RviewHolder(private val mView : View) : RecyclerView.ViewHolder(mView){
        fun bind(str:String){
            val textView = mView.findViewById<TextView>(R.id.text_test)
            textView.setText(str)
            }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RviewHolder {
        Log.e(TAG,"view created")
        val view = LayoutInflater.from(parent.context).inflate(R.layout.test_model,parent,false)
        return RviewHolder(view)
    }

    override fun onBindViewHolder(holder: RviewHolder, position: Int) {
        holder.bind(oldList[position])
    }

    override fun getItemCount() = oldList.size

    fun submitList(newList : ArrayList<String>){
        val cb = object : DiffUtil.Callback(){
            override fun getOldListSize(): Int {
                return oldList.size
            }

            override fun getNewListSize(): Int {
                return newList.size
            }

            override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
                return oldList[oldItemPosition]==(newList[newItemPosition])
            }

            override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
                return oldList[oldItemPosition].equals(newList[newItemPosition])
            }

        }
        val calc = DiffUtil.calculateDiff(cb)
        oldList = newList
        Log.e(TAG,oldList.size.toString())
        calc.dispatchUpdatesTo(this)
        this.notifyDataSetChanged()
    }

    fun appendWord(str : String){
        oldList.add(str)
        this.notifyItemInserted(oldList.size-1)
    }
}