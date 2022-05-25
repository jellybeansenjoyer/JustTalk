package com.example.justtalk.Kotlin.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.CompoundButton
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.justtalk.Kotlin.models.User
import com.example.justtalk.R
import com.example.justtalk.databinding.FreindSelViewBinding
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage

class SelectListAdapter() : RecyclerView.Adapter<SelectListAdapter.SelectListViewHolder>() {
    private var oldList:List<User> = emptyList<User>()
    class SelectListViewHolder(private val view: View,private val context:Context):RecyclerView.ViewHolder(view){
        private var mBinding:FreindSelViewBinding
        companion object{
            val mList:ArrayList<User> = ArrayList()
        }
        init {
            mBinding = DataBindingUtil.bind(view)!!
        }
        fun bind(user:User){
            Firebase.storage.reference.child("UserDP/${user.uid}.jpg").let {
                it.downloadUrl.addOnCompleteListener {
                    if(it.isSuccessful){
                        Glide.with(context).load(it.result!!).into(mBinding.userDp)
                    }
                }
            }
            mBinding.userName.setText(user.name)
            mBinding.checkbox.setOnCheckedChangeListener(object:CompoundButton.OnCheckedChangeListener{
                override fun onCheckedChanged(p0: CompoundButton?, p1: Boolean) {
                    if(p1){
                        mList.add(user)
                    }else{
                        mList.contains(user).let {
                            mList.remove(user)
                        }
                    }
                }
            })
        }
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SelectListViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.freind_sel_view,parent,false)
        return SelectListViewHolder(view,parent.context)
    }

    override fun onBindViewHolder(holder: SelectListViewHolder, position: Int) {
        holder.bind(oldList.get(position))
    }
    override fun getItemCount() = oldList.size
    fun submitList(newlist:List<User>){
        val diffutilcb = object: DiffUtil.Callback(){
            override fun getOldListSize() = oldList.size

            override fun getNewListSize() = newlist.size

            override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
                return oldList.get(oldItemPosition).id.equals(newlist.get(newItemPosition).id)
            }
            override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
                return oldList.get(oldItemPosition).equals(newlist.get(newItemPosition))
            }
        }
        val calc = DiffUtil.calculateDiff(diffutilcb)
        oldList = newlist
        calc.dispatchUpdatesTo(this)
    }

}
interface CheckedListener{
    fun sendFriends(users:List<User>)
}