package com.example.justtalk.test

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.justtalk.R
import com.example.justtalk.databinding.LayoutTestForRemovalBinding

class RecyclerViewRemoveIt : Fragment(){
lateinit private var mBinding : LayoutTestForRemovalBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.layout_test_for_removal,container,false)
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mAdapter = AdapterForTest(){
            Toast.makeText(this.context, "Clicked this ${it}", Toast.LENGTH_SHORT).show()
        }
        mBinding.listTest.apply{
            layoutManager = LinearLayoutManager(this@RecyclerViewRemoveIt.context)
            mAdapter.submitList(list())
            adapter = mAdapter
        }
    }

    companion object{
        fun list() = listOf<String>("Raghav","Rachit","Sachitra","Manas","Unitti","Pranjal","Karanveer","Shrinaath","Bhagat")
    }
}