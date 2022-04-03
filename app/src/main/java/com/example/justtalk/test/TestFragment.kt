package com.example.justtalk.test

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.justtalk.R

class TestFragment : Fragment() {
    private var list = ArrayList<String>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        list = loadList()
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_test, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val rview = view.findViewById<RecyclerView>(R.id.rview)
        val mAdapter = com.example.justtalk.Kotlin.Adapter.SimpleAdapter()
        val editText = view.findViewById<EditText>(R.id.word_field)
        val send = view.findViewById<Button>(R.id.attach)
        rview.apply{
            layoutManager = LinearLayoutManager(this@TestFragment.context)
            mAdapter.submitList(list)
            adapter = mAdapter
        }
        send.setOnClickListener {
            val text = editText.text.toString()
            list.add(text)
            mAdapter.submitList(list)
        }
    }
    companion object{
        fun loadList() = arrayListOf<String>("A","B","C","D","E","F","G","H","I")
    }
}