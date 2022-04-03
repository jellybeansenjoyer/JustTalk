package com.example.justtalk.Kotlin.UI

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.justtalk.Kotlin.Adapter.TestAdapter
import com.example.justtalk.R
import com.example.justtalk.databinding.FragmentRecyclerViewTestBinding
import com.example.justtalk.test.ViewModelTest
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

private const val TAG = "RecyclerViewTestFragmen"
class RecyclerViewTestFragment : Fragment() {
    private var flag = false
    lateinit private var mBinding: FragmentRecyclerViewTestBinding
    lateinit private var mReference : DatabaseReference
    lateinit private var mModel : ViewModelTest
    private var list = ArrayList<String>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mReference = Firebase.database.reference.child("test")
        mModel = ViewModelProvider(this).get(ViewModelTest::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_recycler_view_test,container,false)
        mBinding = DataBindingUtil.bind(view)!!
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val mAdapter = TestAdapter()

        mModel._listOfWords.observe(this){
            Log.e(TAG,it.size.toString())
            if(it.size>0)
            mAdapter.submitlist(it)
        }

        mReference.addChildEventListener(object : ChildEventListener{
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                snapshot.getValue(String::class.java)?.let{
                    mModel.setListModified(it)
                }
            }
            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
            }
            override fun onChildRemoved(snapshot: DataSnapshot) {
            }
            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {
            }
            override fun onCancelled(error: DatabaseError) {
            }
        })
        mBinding.recyclerViewTest.apply{
            layoutManager = LinearLayoutManager(this@RecyclerViewTestFragment.context)
            adapter = mAdapter
        }

        mBinding.controlButtonStop.setOnClickListener {
            flag = true
        }

        mBinding.controlButtonStart.setOnClickListener{
            generateValues()
        }
    }

    fun generateValues(){
        flag = false
        lifecycleScope.launch{
            while (!flag){
                delay(5000)
                mReference.push().setValue(System.currentTimeMillis().toString())
            }
        }
    }
}