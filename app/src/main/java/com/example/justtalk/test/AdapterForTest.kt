package com.example.justtalk.test

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.justtalk.R
import com.example.justtalk.databinding.LayoutTestForRemovalBinding
import com.example.justtalk.databinding.ModelViewBinding

private const val TAG = "AdapterForTest"
class AdapterForTest(private val func: (String) -> Unit) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private var mList : ArrayList<String> = ArrayList<String>()
    val diff = object: DiffUtil.ItemCallback<String>(){
        override fun areItemsTheSame(oldItem: String, newItem: String): Boolean {
            return oldItem.equals(newItem)
        }

        override fun areContentsTheSame(oldItem: String, newItem: String): Boolean {
            return oldItem.equals(newItem)
        }
    }

    val differ = AsyncListDiffer(this,diff)
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as ViewHolderForTest).bind(differ.currentList.get(position),func){str:String,pos:Int->
            val temp = ArrayList<String>()
            temp.addAll(differ.currentList)
            temp.removeAt(pos)
            differ.submitList(temp)
        }
    }

    override fun getItemCount(): Int {
       return differ.currentList.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return ViewHolderForTest.getInstance(parent)
    }

    fun submitList(list:List<String>){
        differ.submitList(list)
    }

}

class ViewHolderForTest(private val view : View) : RecyclerView.ViewHolder(view){
     private var mBinding : ModelViewBinding
    init{
        mBinding = DataBindingUtil.bind(view)!!
    }
    fun bind(str:String,func:(String)->Unit,func2:(String,Int)->Unit){
        mBinding.name.setText(str)
        onItemClickListener = func
        view.setOnClickListener{
            Log.e(TAG,position.toString())
            func2.invoke(str,position)
        }
        view.setOnLongClickListener {
            onItemClickListener?.let{
                it(str)
            }
                true
        }
    }
    companion object{
        @JvmStatic
        fun getInstance(parent:ViewGroup): ViewHolderForTest{
            val view = LayoutInflater.from(parent.context).inflate(R.layout.model_view,parent,false)
            return ViewHolderForTest(view)
        }
    }

    fun setOnItemClick(func:(String)->Unit){
        onItemClickListener = func
    }
    var onItemClickListener:((String)->Unit)? = null
}