package com.example.justtalk.JAVA;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.example.justtalk.R;
import com.example.justtalk.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {
    public ActivityMainBinding mBinding;
    @Override
    protected void onCreate(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);
    }
    public <T extends Fragment> void makeTransaction(Class<T> fragmentClass,Bundle bundle){
        if(bundle==null)
            return;
        getSupportFragmentManager().beginTransaction().add(R.id.container,fragmentClass,bundle).addToBackStack("ChatFragment").commit();
    }
}
