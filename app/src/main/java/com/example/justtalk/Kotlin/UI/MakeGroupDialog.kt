package com.example.justtalk.Kotlin.UI

import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ListAdapter
import android.widget.Toast
import androidx.compose.ui.layout.Layout
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.justtalk.Kotlin.models.User
import com.example.justtalk.R
import com.example.justtalk.databinding.GroupDialogLayoutBinding

class MakeGroupDialog : DialogFragment() {
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let{
            val listTemp= emptyList<String>()
           AlertDialog.Builder(it).setTitle("Group Details").apply{
               val view  = LayoutInflater.from(it).inflate(R.layout.group_dialog_layout,null)
               val binding:GroupDialogLayoutBinding = DataBindingUtil.bind(view)!!
               val user = User()
               setView(view)
               setPositiveButton("Create",object:DialogInterface.OnClickListener{
                   override fun onClick(p0: DialogInterface?, p1: Int) {
                       if(!binding.nameOfGroup.text.isEmpty())
                           user.name = binding.nameOfGroup.text.toString()
                       else
                           Toast.makeText(it, "Field cannot be empty", Toast.LENGTH_SHORT).show()
                       binding.groupMemberDp.adapter = object:ArrayAdapter<String>(it,R.layout.model_group_dp,listTemp){
                           override fun getView(
                               position: Int,
                               convertView: View?,
                               parent: ViewGroup
                           ): View {
                                val view =  LayoutInflater.from(it).inflate(R.layout.model_group_dp,parent)
                                view.findViewById<de.hdodenhof.circleimageview.CircleImageView>(R.id.user_dp).run{
                                    Glide.with(it).load(listTemp[position]).into(this)
                                }
                               return view
                           }
                       }
                   }
               })
           }

        }!!.create()
    }
}